package com.kortisan.framework.redux.middleware
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.actions.RemoteConfigActions
import com.kortisan.framework.redux.controllers.ControllersProxy
import com.kortisan.framework.redux.stores.ApplicationStateStore
import com.kortisan.framework.redux.controllers.remoteConfig.RemoteConfigController
object RemoteConfigMiddleware: MiddlewareInterface {
    override fun doNext(action: ReduxAction, applicationState: ApplicationStateStore): ReduxAction {
        if ( action is RemoteConfigActions.ReloadAction )
            ControllersProxy.getController<RemoteConfigController>()?.reloadRemoteConfig()

        return action
    }
}