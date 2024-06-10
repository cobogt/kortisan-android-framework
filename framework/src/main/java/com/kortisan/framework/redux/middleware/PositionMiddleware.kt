package com.kortisan.framework.redux.middleware
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.GeopositionActions
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.ControllersProxy
import com.kortisan.framework.redux.controllers.geoposition.GeopositionController
import com.kortisan.framework.redux.stores.ApplicationStateStore

object PositionMiddleware: MiddlewareInterface {
    override fun doNext(action: ReduxAction, applicationState: ApplicationStateStore): ReduxAction {
        if (action is GeopositionActions)
            ControllersProxy.getController<GeopositionController>()?.also { geoController ->
                // TODO: Logica para controlar el GPS
                when (action) {
                    is GeopositionActions.SetGeopositionAppStateAction -> {
                        geoController.reduceApplicationState( action )

                        return ReduxAction.EmptyAction
                    }
                }
            }

        return action
    }
}