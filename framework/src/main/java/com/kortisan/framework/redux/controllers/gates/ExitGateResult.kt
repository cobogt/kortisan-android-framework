package com.kortisan.framework.redux.controllers.gates
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.ReduxAction

/**
 * Acción para salida de un subflujo con un resultado
 *
 * Puede tener una acción resultado que reemplazará a la acción original, es necesario en
 * caso de que una flujo que haya invocado un subflujo requiera reducir un estado a partir
 * de una acción resultado del subflujo.
 *
 * Esto puede implicar que los gates tengan su catálogo de acciones resultado para que los
 * flujos puedan reducir estos valores.
 *
 * Adicionalmente pueden producir valores crudos para otras operaciones.
*/
sealed interface ExitGateResult<T> {
    val rawDataResult: T?
    data class Success<T>(
        val customResultSuccessAction: ReduxAction? = null,
        override val rawDataResult:    T? = null
    ): ExitGateResult<T>
    data class Fail<T>   (
        val customResultFailAction:    ReduxAction? = null,
        override val rawDataResult:    T? = null
    ): ExitGateResult<T>
    data class Retry<T>  (
        val customResultRetryAction:   ReduxAction? = null,
        override val rawDataResult:    T? = null
    ): ExitGateResult<T>
}