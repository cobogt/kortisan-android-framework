package com.kortisan.framework.redux.actions

sealed class PerformanceScoreActions: ReduxAction() {
    object CalculateScore: PerformanceScoreActions()
    data class SetPerformanceScoreAction( val newScore: Int ): PerformanceScoreActions()
}
