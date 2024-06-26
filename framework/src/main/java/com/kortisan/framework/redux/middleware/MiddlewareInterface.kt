package com.kortisan.framework.redux.middleware
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.stores.ApplicationStateStore

fun interface MiddlewareInterface {
    fun doNext(action: ReduxAction, applicationState: ApplicationStateStore): ReduxAction
}