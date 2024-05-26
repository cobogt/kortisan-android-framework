package com.kortisan.framework.redux.state

import com.kortisan.framework.entities.Device

sealed class DeviceState: ReduxState() {
    object Loading: DeviceState()
    data class Initialized(
        val device: Device
    ): DeviceState()
}