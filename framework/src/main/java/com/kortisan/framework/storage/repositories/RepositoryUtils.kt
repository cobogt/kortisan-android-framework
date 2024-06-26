package com.kortisan.framework.storage.repositories
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

/**
 * Clase con utilidades para realizar las operaciones de los repositorios
 */
abstract class RepositoryUtils {
    open val dispatcher: CoroutineDispatcher = Dispatchers.IO
    /**
     * Algoritmo 7: Ejecución periodica con condición de salida
     */
    fun <T> periodicRun(
        task:            suspend () -> T,
        finishCondition: suspend () -> Boolean,
        timeInSeconds:   Long = 1L
    ): Flow<T> =
        callbackFlow {
            do {
                send( task.invoke() )

                delay( timeInSeconds )
            } while( finishCondition.invoke() )
        }.flowOn( dispatcher )
}
