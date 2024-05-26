package com.kortisan.framework.redux.controllers.performanceScrore

import com.kortisan.framework.redux.actions.PerformanceScoreActions
import com.kortisan.framework.redux.controllers.ReduxControllerAbstract
import kotlin.random.Random

class CalculatePerformanceController: ReduxControllerAbstract() {
    fun calculateScore() {
        reduceApplicationState(
            PerformanceScoreActions.SetPerformanceScoreAction(
                Random.nextInt(0, 100)
            )
        )
    }
}