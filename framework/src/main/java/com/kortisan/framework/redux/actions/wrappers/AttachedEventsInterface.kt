package com.kortisan.framework.redux.actions.wrappers
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.ReduxAction

/**
 * Lista de modificadores que se pueden asociar a un componente
 * https://developer.android.com/jetpack/compose/modifiers-list?hl=es-419
 */
interface AttachedEventsInterface {
    val onClick:              ReduxAction?
    val onFocus:              ReduxAction?
    val onKeyEvent:           ReduxAction?
    val onLongClick:          ReduxAction?
    val onDoubleClick:        ReduxAction?
    val onSizeChanged:        ReduxAction?
    val onFocusChanged:       ReduxAction?
    val onPreviewKeyEvent:    ReduxAction?
    val onGloballyPositioned: ReduxAction?

    // TODO: Crear envoltorio para asociar estos eventos a los estados
    val onSwipe:              ReduxAction?
    val onDragEnd:            ReduxAction?
    val onScrollEnd:          ReduxAction?
    val onDragStart:          ReduxAction?
    val onPanChange:          ReduxAction?
    val onZoomChange:         ReduxAction?
    val onScrollStart:        ReduxAction?
    val onRotationChange:     ReduxAction?

    // TODO: Definir en el callback del un objeto seleccionable
    val onSelected:           ReduxAction?
}
