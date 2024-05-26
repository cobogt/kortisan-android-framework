package com.kortisan.framework.redux.controllers.tagging.decorators.application

import com.kortisan.framework.redux.controllers.tagging.decorators.SceneBaseDecorator
import com.kortisan.framework.redux.controllers.tagging.decorators.SceneDecoratorInterface
import com.kortisan.framework.redux.state.SensorsState

/**
 * Este decorador de taggeo agrega la información de los sensores del dispositivo
 * cuando esten disponibles.
 */
class SensorsTaggingDecorator(
    override val sceneDecorator: SceneDecoratorInterface,
): SceneBaseDecorator() {
    fun setSensorsState ( state: SensorsState ): SensorsTaggingDecorator =
        apply {
            if( state is SensorsState.DataAcquired ) {
                decoratedValues["sensorRefreshRateSeconds"] =
                    "${state.refreshRateSeconds}"

                decoratedValues["sensorLinearAcc"] =
                    "${state.linearAcc}"

                decoratedValues["sensorPressureHpa"] =
                    "${state.pressureHpa}"

                decoratedValues["sensorAmbientLightLx"] =
                    "${state.ambientLightLx}"

                decoratedValues["sensorMagneticFieldUt"] =
                    "${state.magneticFieldUt}"

                decoratedValues["sensorRelativeHumidity"] =
                    "${state.relativeHumidity}"

                decoratedValues["sensorDeviceTemperatureCelsius"] =
                    "${state.deviceTemperatureCelsius}"

                decoratedValues["sensorAmbientTemperatureCelsius"] =
                    "${state.ambientTemperatureCelsius}"
            }
        }
}
