package com.kortisan.framework.redux.actions
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.IntSize

sealed class EventsActions: ReduxAction() {
    data class OnKeyEvent           ( val keyEvent: KeyEvent? ): EventsActions()
    data class OnSizeChanged        ( val intSize: IntSize? ): EventsActions()
    data class OnFocusChanged       ( val focusState: FocusState? ): EventsActions()
    data class OnFocusEventAction   ( val focusState: FocusState? ): EventsActions()
    data class OnPreviewKeyEvent    ( val onPreviewKeyEvent: KeyEvent? ): EventsActions()
    data class OnGloballyPositioned ( val layoutCoordinates: LayoutCoordinates? ): EventsActions()
}
