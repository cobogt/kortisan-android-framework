package com.kortisan.framework.bloc

import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso base para extender a los demás
 * Tipo I: Valor de entrada
 * Tipo O: Valor de salida
 * Tipo T: Valor de los parámetros del caso de uso
 */
interface BaseUseCase<I, O, P> {
    /**
     * Establece los parámetros para el funcionamiento del caso de uso
     */
    fun setParams( params: P )

    /**
     * Recibe un flujo de recursos y produce otro flujo de recursos depués de transformarlo
     * a partir de los parámetros definidos en la implementación del caso de uso.
     */
    fun transformResource( inputData: Flow<Resource<I>> ): Flow<Resource<O>>
    fun transform( inputData: Flow<I> ): Flow<O>
}