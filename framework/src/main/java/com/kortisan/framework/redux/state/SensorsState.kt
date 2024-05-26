package com.kortisan.framework.redux.state

import com.kortisan.framework.redux.state.productionrules.ProductionRule

/**
 * Contiene la información relacionada con los sensores de hardware
 */
sealed class SensorsState: ReduxState() {
    data object Loading: SensorsState() {
        override val productionRules: List<ProductionRule> = commonProductionRules
    }

    data class DataAcquired(
        val refreshRateSeconds:        Int = 60,
        val linearAcc:                 Int,
        val pressureHpa:               Int,
        val ambientLightLx:            Int,
        val magneticFieldUt:           Int,
        val relativeHumidity:          Int,
        val deviceTemperatureCelsius:  Int,
        val ambientTemperatureCelsius: Int,
    ): SensorsState() {
        override val productionRules: List<ProductionRule> = commonProductionRules
    }

    protected val commonProductionRules: List<ProductionRule> = listOf(

    )
}
