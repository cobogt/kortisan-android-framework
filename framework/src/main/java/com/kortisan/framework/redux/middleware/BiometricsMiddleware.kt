package com.kortisan.framework.redux.middleware

import com.kortisan.framework.redux.actions.BiometricsActions
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.ControllersProxy
import com.kortisan.framework.redux.controllers.securityAssets.SecurityAssetsController
import com.kortisan.framework.redux.stores.ApplicationStateStore

object BiometricsMiddleware: MiddlewareInterface {
    override fun doNext(action: ReduxAction, applicationState: ApplicationStateStore): ReduxAction {
        if( action is BiometricsActions ) {
            val controller = ControllersProxy.getController<SecurityAssetsController>()

            when( action ) {
                BiometricsActions.AskBiometricsAction -> controller?.askForBiometrics()
            }
        }
        return action
    }
}