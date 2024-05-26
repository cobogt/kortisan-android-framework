package com.kortisan.authentication.presentation.actions

import com.kortisan.framework.redux.actions.ReduxAction

data class SetTextAction(
    val newText: String
): ReduxAction()
