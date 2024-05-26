package com.kortisan.framework.redux.actions

/**
 * Esta acción está diseñada para cuando un controlador o middleware quiere que se vuelva a
 * procesar una acción resultado sin que esta llegue a donde se originó la primera.
 *
 * Reinicia el ciclo del despachador.
 */
data class ReDispatchAction(
    val action: ReduxAction
): ReduxAction()