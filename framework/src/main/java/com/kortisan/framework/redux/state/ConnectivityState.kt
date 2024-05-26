package com.kortisan.framework.redux.state

sealed class ConnectivityState: ReduxState() {
    object NoInternet:          ConnectivityState()
    object ConnectedNoInternet: ConnectivityState()
    object OnlineNoService:     ConnectivityState()
    object Online:              ConnectivityState()
}