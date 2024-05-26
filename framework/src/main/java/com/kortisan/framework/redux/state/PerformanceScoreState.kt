package com.kortisan.framework.redux.state

import com.kortisan.framework.redux.actions.PerformanceScoreActions
import com.kortisan.framework.redux.state.productionrules.ProductionRule

sealed class PerformanceScoreState: ReduxState() {
    data class Calculated( val score: Int ): PerformanceScoreState(){
        override val productionRules: List<ProductionRule> =
            listOf { state, action ->
                when ( action ) {
                    is PerformanceScoreActions.SetPerformanceScoreAction ->
                        Calculated( action.newScore )
                    else -> state
                }
            }
    }

    object NotCalculated:                    PerformanceScoreState() {
        override val productionRules: List<ProductionRule> =
            listOf { state, action ->
                when ( action ) {
                    is PerformanceScoreActions.SetPerformanceScoreAction ->
                        Calculated( action.newScore )
                    else -> state
                }
            }
    }
}