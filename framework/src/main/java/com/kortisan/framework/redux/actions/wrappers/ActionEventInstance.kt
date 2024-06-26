package com.kortisan.framework.redux.actions.wrappers
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.ReduxAction

data class ActionEventInstance(
    override val onClick:              ReduxAction? = null,
    override val onFocus:              ReduxAction? = null,
    override val onKeyEvent:           ReduxAction? = null,
    override val onLongClick:          ReduxAction? = null,
    override val onDoubleClick:        ReduxAction? = null,
    override val onSizeChanged:        ReduxAction? = null,
    override val onFocusChanged:       ReduxAction? = null,
    override val onPreviewKeyEvent:    ReduxAction? = null,
    override val onGloballyPositioned: ReduxAction? = null,
    override val onSwipe:              ReduxAction? = null,
    override val onDragEnd:            ReduxAction? = null,
    override val onScrollEnd:          ReduxAction? = null,
    override val onDragStart:          ReduxAction? = null,
    override val onPanChange:          ReduxAction? = null,
    override val onZoomChange:         ReduxAction? = null,
    override val onScrollStart:        ReduxAction? = null,
    override val onRotationChange:     ReduxAction? = null,
    override val onSelected:           ReduxAction? = null,
): AttachedEventsInterface