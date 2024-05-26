package com.kortisan.framework.redux.actions

sealed class BiometricsActions: ReduxAction() {
    object AskBiometricsAction: BiometricsActions()

}
