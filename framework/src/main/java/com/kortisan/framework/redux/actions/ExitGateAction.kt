package com.kortisan.framework.redux.actions

import com.kortisan.framework.redux.controllers.gates.BaseGate
import com.kortisan.framework.redux.controllers.gates.ExitGateResult

/**
 * Acción para salida de un subflujo con un resultado.
*/
data class ExitGateAction<T> (
    val result: ExitGateResult<T>
): ReduxAction() {
    data class SetRawDataResultAction<T> (
        val gate:           BaseGate,
        val originalAction: ReduxAction,
        val resultAction:   ReduxAction,
        val data:           T
    ): ReduxAction()
}