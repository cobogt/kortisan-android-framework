package com.kortisan.framework.redux.actions
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

/**
 * Acción opcional que pueden contener algunas acciones para
 * poder 'deshacer' su efecto en el estado de la aplicación
 */
open class RevertAction: ReduxAction()