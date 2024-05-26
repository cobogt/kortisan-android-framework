package com.kortisan.framework.redux.controllers

import com.kortisan.framework.redux.state.ReduxState

sealed class ReduxControllerState: ReduxState() {
    object Stopped: ReduxControllerState()
    object Starting: ReduxControllerState()
    object Started: ReduxControllerState()
    data class ErrorOnStart(
        val errorMessage: String,
        val exception: Exception? = null
    ): ReduxControllerState()
}