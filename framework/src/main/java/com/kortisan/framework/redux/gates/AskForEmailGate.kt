package com.kortisan.framework.redux.gates

import com.kortisan.framework.redux.actions.DefaultErrorAction
import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.gates.BaseGate
import com.kortisan.framework.redux.controllers.navigation.targets.AuthenticationNavigationGroup

object AskForEmailGate: BaseGate() {
    override val startAction: ReduxAction =
        NavigationActions.NavigateToTarget(
            AuthenticationNavigationGroup.Authentication.AskForEmailCompose
        )

    override val onFailAction: ReduxAction =
        DefaultErrorAction("Error al solicitar el Email")

    override fun enterInGate(startAction: ReduxAction): Boolean {
        return true
    }
}