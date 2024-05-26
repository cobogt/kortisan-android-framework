package com.kortisan.framework.redux.gates

import com.kortisan.framework.R
import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.actions.NotificationActions
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.gates.BaseGate
import com.kortisan.framework.redux.controllers.navigation.targets.AuthenticationNavigationGroup
import com.kortisan.framework.redux.state.SessionState
import com.kortisan.framework.redux.stores.ApplicationStateStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

/**
 * Puerta para comprobar que el usuario haya iniciado sesión
 */
object LoggedGate: BaseGate() {
    override val startAction: ReduxAction =
        NavigationActions.NavigateToTarget(
            AuthenticationNavigationGroup.Authentication.LoginCompose
        )

    override val onFailAction: ReduxAction =
        NotificationActions.ShowNotification(
            "Error en LoggedGate",
            "No se puede continuar con el login",
            "APP",
            smallIcon = R.drawable.error_24
        )

    override val subGates: List<BaseGate> = listOf(
//        AskForEmailGate
    )

    override fun enterInGate(startAction: ReduxAction): Boolean {
        return runBlocking {
            return@runBlocking (
                ApplicationStateStore.sessionState.first() is SessionState.LoggedOut
            )
        }
    }
}
