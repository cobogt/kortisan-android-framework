package com.kortisan.framework.redux.middleware

import com.kortisan.framework.redux.actions.DefaultErrorAction
import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.actions.ReDispatchAction
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.navigation.targets.DashboardNavigationGroup
import com.kortisan.framework.redux.stores.ApplicationStateStore

object ErrorHandlerMiddleware: MiddlewareInterface {
    override fun doNext(action: ReduxAction, applicationState: ApplicationStateStore): ReduxAction =
        if( action is DefaultErrorAction ) {
            ReDispatchAction(
                NavigationActions.NavigateToTargetWithParams(
                    DashboardNavigationGroup.ShowError,
                    mapOf(
                        "title"            to action.title,
                        "description"      to action.description,
                        "exceptionName"    to "${action.exception?.javaClass?.simpleName}",
                        "exceptionMessage" to "${action.exception?.localizedMessage}",
                    ).plus( action.extras )
                )
            )
        } else
            action
}