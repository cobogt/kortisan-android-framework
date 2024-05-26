package com.kortisan.framework.redux.actions.wrappers
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import com.kortisan.framework.bloc.ViewModelActionDispatcher
import com.kortisan.framework.redux.controllers.tagging.decorators.AttachEventDecorator
import com.kortisan.framework.redux.controllers.tagging.decorators.SceneDecoratorInterface

/**
 * Esta función de extensión se encarga de asociar los eventos de un componente a una acción
 * y luego enviar esta acción al despachador junto con la información de la escena a travez del
 * despachador de acciones del viewModel
 */
@SuppressLint("UnnecessaryComposedModifier")
@OptIn(ExperimentalFoundationApi::class)
@Stable
fun Modifier.attachEventWrapper(
    attachedEvents: AttachedEventsInterface,
    dispatcher: ViewModelActionDispatcher,
    scene: SceneDecoratorInterface,
    callback: (() -> Unit)? = null
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "AttatchEventWrapper"
        value = attachedEvents
    }) {

    /**
     * Los eventos que reciban un parámetro, deben poder identificar la acción y transformarla
     * en una nueva del mismo tipo pero capaz de llevar los parámetros recibidos en el evento
     */
//    attachedEvents.onFocus?.apply {
//        tagging = AttachEventDecorator(scene, "focus")
//        // TODO: Envolver y reemplazar el action en un EventsActions para reducirlo en el árbol redux
//        onFocusEvent { focusState ->
//            dispatcher.dispatch( this )
//        }
//    }

//    onFocusChanged { focusState ->
//
//    }
//
//    onSizeChanged { intSize ->
//
//    }
//
//    onGloballyPositioned { layoutCoordinates ->
//
//    }

    /**
     * Existen eventos que se propagan hacia el padre, pudiendo detener esta propagación desde
     * el momento en que se intercepta.
     */
//    onKeyEvent { keyEvent ->
//        false
//    }

    /**
     * This callback is invoked when the user interacts with the hardware keyboard.
     * It gives ancestors of a focused component the chance to intercept a KeyEvent.
     * Return true to stop propagation of this event. If you return false, the key
     * event will be sent to this onPreviewKeyEvent's child. If none of the children
     * consume the event, it will be sent back up to the root KeyInputModifier
     * using the onKeyEvent callback.
     */
//    onPreviewKeyEvent { keyEvent ->
//        false
//    }

    /**
     * enabled - Controls the enabled state. When false, onClick, onLongClick or
     * onDoubleClick won't be invoked.
     * onClickLabel - semantic / accessibility label for the onClick action
     * role - the type of user interface element. Accessibility services might use this to
     * describe the element or do customizations.
     * onLongClickLabel - semantic / accessibility label for the onLongClick action
     * onLongClick - will be called when user long presses on the element
     * onDoubleClick - will be called when user double clicks on the element
     * onClick - will be called when user clicks on the element
     **/
    combinedClickable (
        enabled = true, null, null, null,
        onClick = attachedEvents.onClick?.let { {
            callback?.invoke()
            it.apply { tagging = AttachEventDecorator(scene, "click") }
                .also { dispatcher.dispatch( it ) }
        }} ?: {},
        onDoubleClick = attachedEvents.onDoubleClick?.let { {
            it.apply { tagging = AttachEventDecorator(scene, "doubleClick") }
                .also { dispatcher.dispatch( it ) }
        } },
        onLongClick = attachedEvents.onLongClick?.let { {
            it.apply { tagging = AttachEventDecorator(scene, "longClick") }
                .also { dispatcher.dispatch( it ) }
        } }
    )

    // Requieren State
    // TODO: Crear envoltorio para asociarlo a los evetnos de los states de estas interacciones
    /*
    scrollable (ScrollableState { it }, Orientation.Horizontal)
    transformable (TransformableState { zoomChange, panChange, rotationChange ->  })
    swipeable (SwipeableState())
    draggable(DraggableState {  })*/
}