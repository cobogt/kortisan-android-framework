package com.kortisan.framework.redux.state

import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.ControllersProxy
import com.kortisan.framework.redux.controllers.navigation.NavigationController
import com.kortisan.framework.redux.controllers.navigation.protocol.NavigationTarget
import com.kortisan.framework.redux.controllers.navigation.targets.TargetActivity
import com.kortisan.framework.redux.controllers.navigation.targets.TargetClass
import com.kortisan.framework.redux.controllers.navigation.targets.TargetCompose
import com.kortisan.framework.redux.controllers.navigation.targets.TargetExternal
import com.kortisan.framework.redux.state.productionrules.ProductionRule

sealed class NavigationState: ReduxState() {
    data class NavigateToActivity(
        val previousState:    NavigationState,
        val actionDispatched: ReduxAction,
        val className:        String,
        val params:           Map<String, String> = mapOf()
    ): NavigationState()

    object LoadingTBD: NavigationState()

    override val productionRules: List<ProductionRule> =
        listOf { state, action ->
            if (action is NavigationActions && state is NavigationState) {
                when ( action ) {
                    is NavigationActions.NavigateToClass ->
                        NavigateToActivity(
                            state,
                            action,
                            action.className
                        )

                    is NavigationActions.NavigateToRoute ->
                        ControllersProxy.getController<NavigationController>()
                            ?.getNavigationTargetForRoute( action.route )
                            ?.let {
                                NavigateToActivity(
                                    state,
                                    action,
                                    getClassNameForTarget( it ),
                                    action.params
                                )
                            }?: state

                    is NavigationActions.NavigateToTarget ->
                        NavigateToActivity(
                            state,
                            action,
                            getClassNameForTarget( action.target ),
                            mapOf()
                        )

                    is NavigationActions.NavigateToTargetWithParams ->
                        NavigateToActivity(
                            state,
                            action,
                            getClassNameForTarget( action.target ),
                            action.params
                        )
                }
            } else
                state
        }

    private fun getClassNameForTarget( target: NavigationTarget ): String =
        when( target ) {
            is TargetCompose<*> -> target.targetActivity.primitiveTargetActivityIdentifier.className
            is TargetActivity   -> target.primitiveTargetActivityIdentifier.className
            is TargetClass      -> target.classInPackage
            is TargetExternal   -> ""
            else                -> ""
        }
}