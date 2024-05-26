package com.kortisan.framework.redux.actions

sealed class SensorsActions: ReduxAction() {
    data class SetRefreshRateAction(
        val refreshRate: Int = 60
    ): SensorsActions()

    data class StoreSensorsAppStateAction(
        val linearAcc:                 Int,
        val pressureHpa:               Int,
        val ambientLightLx:            Int,
        val magneticFieldUt:           Int,
        val relativeHumidity:          Int,
        val deviceTemperatureCelsius:  Int,
        val ambientTemperatureCelsius: Int,
    ): SensorsActions()
}
