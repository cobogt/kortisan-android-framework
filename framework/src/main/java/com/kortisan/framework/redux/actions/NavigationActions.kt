package com.kortisan.framework.redux.actions
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.controllers.navigation.protocol.NavigationTarget

sealed class NavigationActions: ReduxAction() {

    data class NavigateToTarget(
        val target: NavigationTarget
    ): NavigationActions()

    data class NavigateToTargetWithParams(
        val target: NavigationTarget,
        val params: Map<String, String>
    ): NavigationActions()

    data class NavigateToClass(
        val className: String,
        val params:    Map<String, String>
    ): NavigationActions()

    data class NavigateToRoute(
        val route:  String,
        val params: Map<String, String>
    ): NavigationActions()
}