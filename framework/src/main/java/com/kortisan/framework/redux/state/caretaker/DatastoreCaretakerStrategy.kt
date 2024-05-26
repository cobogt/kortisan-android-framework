package com.kortisan.framework.redux.state.caretaker

import com.kortisan.framework.redux.state.ReduxState

class DatastoreCaretakerStrategy(
    override val defaultState: ReduxState
): CaretakerStrategy() {
    override fun persist(currentState: ReduxState) {
        TODO("Not yet implemented")
    }

    override fun recover(stateClassName: String): ReduxState? {
        TODO("Not yet implemented")
    }
}