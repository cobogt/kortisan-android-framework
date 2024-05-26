package com.kortisan.framework.redux.gates

import com.kortisan.framework.redux.actions.BiometricsActions
import com.kortisan.framework.redux.actions.DefaultErrorAction
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.gates.BaseGate

object FingerprintGate: BaseGate() {
    override val startAction: ReduxAction =
        BiometricsActions.AskBiometricsAction

    override val onFailAction: ReduxAction
        get() = DefaultErrorAction("No se puede solicitar la autenticación")

    override fun enterInGate(startAction: ReduxAction): Boolean {
        return true
    }
}