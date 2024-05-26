package com.kortisan.framework.redux.middleware

import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.actions.SensorsActions
import com.kortisan.framework.redux.controllers.ControllersProxy
import com.kortisan.framework.redux.controllers.sensors.SensorsController
import com.kortisan.framework.redux.stores.ApplicationStateStore

object SensorsMiddleware: MiddlewareInterface {
    override fun doNext(action: ReduxAction, applicationState: ApplicationStateStore): ReduxAction {
        if( action is SensorsActions.SetRefreshRateAction )
            ControllersProxy.getController<SensorsController>()
                ?.setRefreshRate( action.refreshRate )

        return action
    }
}