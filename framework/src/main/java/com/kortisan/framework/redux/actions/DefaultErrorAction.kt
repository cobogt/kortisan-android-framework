package com.kortisan.framework.redux.actions

/**
 * Esta acción captura la información de un error de forma predeterminada
 * Es procesada en el middleware de captura de errores
 */
data class DefaultErrorAction(
    val title:       String,
    val description: String = "",
    val exception:   Exception? = null,
    val extras:      Map<String, String> = mapOf()
): ReduxAction()
