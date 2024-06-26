package com.kortisan.framework.redux.middleware
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.PerformanceScoreActions
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.performanceScrore.CalculatePerformanceController
import com.kortisan.framework.redux.controllers.ControllersProxy
import com.kortisan.framework.redux.stores.ApplicationStateStore

/**
 * Este Middleware calcula la puntuación del teléfono para saber que tanto comprimir
 * las imágenes y los recursos.
 */
object CalculatePerformanceScoreMiddleware: MiddlewareInterface {
    override fun doNext(action: ReduxAction, applicationState: ApplicationStateStore): ReduxAction =
        if( action is PerformanceScoreActions.CalculateScore ) {
            ControllersProxy.getController<CalculatePerformanceController>()
                ?.calculateScore()

            ReduxAction.EmptyAction
        }else
            action
}