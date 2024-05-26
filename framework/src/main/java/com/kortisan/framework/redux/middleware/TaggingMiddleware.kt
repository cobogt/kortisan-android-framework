package com.kortisan.framework.redux.middleware
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.ControllersProxy
import com.kortisan.framework.redux.stores.ApplicationStateStore
import com.kortisan.framework.redux.controllers.tagging.TaggingController

object TaggingMiddleware: MiddlewareInterface {
    override fun doNext(action: ReduxAction, applicationState: ApplicationStateStore): ReduxAction {
        ControllersProxy.getController<TaggingController>()
            ?.launchTagging( action )

        return action
    }
}