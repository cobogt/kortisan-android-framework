package com.kortisan.framework.storage.repositories
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.bloc.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Tipo R: Tipo de dato del parámetro de entrada para llamar a la fuente remota
 *
 * Tipo S: Resultado de la función de transformación (T -> S)
 *
 * Tipo T: Tipo de dato del resultado de la llamada a la fuente de datos local
 */
sealed interface RepositoryStrategy<R, L, M> {
    /** Llamada a la fuente de datos remota */
    val remoteSourceData: suspend () -> R

    /** Fuente local observable */
    val localSourceData: suspend () -> Flow<L>

    /** Transforma la respuesta de la llamada LOCAL de la capa DATA,
     *  devuelve Modelos de la capa DOMAIN */
    val localToModelTransform: suspend (L) -> M

    /** Transforma la respuesta de la llamada REMOTA de la capa DATA, devuelve
     * Modelos de la  capa DOMINIO */
    val remoteToModelTransform: suspend (R) -> M

    /** Almacena el resultado de la función de transformación */
    val storeRemoteResult: (suspend (R) -> Unit)?

    /**
     * Ejecuta la petición a la fuente remota para luego transformar la respuesta y si está
     * definida la función de almacenamiento se ejecuta con la respuesta transformada.
     *
     * Consume una fuente local observable, y en cada emisión de valores se usa la
     * función de transformación para mapear el resultado. Si está definida la función de
     * almacenamiento, guarda el resultado de la transformación invocandola.
     */
    fun consume(): Flow<M>

    /**
     * Envuelve el resultado del flujo consumido en un recurso
     */
    fun consumeAsResource(): Flow<Resource<M>>

    /**
     * Reintenta la operación de consumo de fuente remota, transformación
     * de respuesta y almacenamiento del resultado transformado.
     *
     * Esta operación se puede ejecutar sin consumir la estrategia si solo se busca
     * hacer la petición, transformar la respuesta y persistir el resultado.
     */
    fun fetch()

    /**
     * Estrategia para leer información de la red y persistirla en local
     */
    data class FetchTransformAndStoreStrategy<R, L, M>(
        override val remoteSourceData: suspend () -> R,
        override val localSourceData: suspend () -> Flow<L>,
        override val localToModelTransform: suspend (L) -> M,
        override val remoteToModelTransform: suspend (R) -> M,
        override val storeRemoteResult: (suspend (R) -> Unit)? = null
    ): RepositoryStrategy<R, L, M> {
        override fun consume(): Flow<M> =
            flow {
                fetch()

                emitAll(
                    localSourceData
                        .invoke()
                        .mapNotNull { localResult ->
                            localResult?.let {
                                localToModelTransform( localResult )
                            }
                        }
                )
            }

        override fun consumeAsResource(): Flow<Resource<M>> =
            consume()
                .transform<M, Resource<M>> { Resource.success( it ) }
                .onStart {
                    emit( Resource.loading() )
                }
                .catch { cause ->
                    emit( Resource.error( Exception( cause ) ))
                }

        override fun fetch() {
            CoroutineScope( Dispatchers.IO ).launch {
                val remoteResponse = remoteSourceData.invoke()
                remoteToModelTransform(
                    remoteResponse
                )

                storeRemoteResult?.invoke( remoteResponse )
            }.start()
        }
    }

    data class FetchAndTransformStrategy<R, M>(
        override val remoteSourceData: suspend () -> R,
        override val remoteToModelTransform: suspend (R) -> M,
        override val storeRemoteResult: (suspend (R) -> Unit)? = null
    ): RepositoryStrategy<R, Any, M> {
        private val fullConsumeStrategy = FetchTransformAndStoreStrategy<R, Any, M>(
            remoteSourceData,
            { flow { emit(Any()) } },
            localToModelTransform,
            remoteToModelTransform,
            storeRemoteResult
        )

        override val localToModelTransform: suspend (Any) -> M
            get() = TODO("Not yet implemented")

        override fun consume(): Flow<M> =
            fullConsumeStrategy.consume()

        override fun consumeAsResource(): Flow<Resource<M>> =
            fullConsumeStrategy.consumeAsResource()

        override fun fetch() =
            fullConsumeStrategy.fetch()

        override val localSourceData: suspend () -> Flow<Any> =
            { flow { emit(Any()) } }
    }
}
