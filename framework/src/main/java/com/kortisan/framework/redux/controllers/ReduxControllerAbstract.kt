package com.kortisan.framework.redux.controllers

import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.stores.ApplicationStateStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class ReduxControllerAbstract {
    private val isStartedMutable: MutableStateFlow<ReduxControllerState> =
        MutableStateFlow<ReduxControllerState>( ReduxControllerState.Stopped )

    open val isStarted: StateFlow<ReduxControllerState> = isStartedMutable

    var currentAction: ReduxAction? = null

    open fun start() =
        setStartStatus( ReduxControllerState.Started )

    fun reduceApplicationState( action: ReduxAction ) =
        ApplicationStateStore.reduceAction( action )

    fun setStartStatus( newState: ReduxControllerState ) {
        isStartedMutable.value = newState
    }
}