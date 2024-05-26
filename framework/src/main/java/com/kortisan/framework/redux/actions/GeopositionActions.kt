package com.kortisan.framework.redux.actions

import com.kortisan.framework.redux.state.GeopositionState

sealed class GeopositionActions: ReduxAction() {
    data class SetGeopositionAppStateAction( val newState: GeopositionState ): GeopositionActions()
}
