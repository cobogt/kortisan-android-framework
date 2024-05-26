package com.kortisan.framework.redux.actions
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

/**
 * Permiten actualizar a la aplicación bajo políticas internas y de la misma play store,
 * haciendo uso del middleware de actualizaciones ya que hay acciones que van a demandar
 * tener la última versión de la aplicación antes de poder ser consumadas.
 *
 * @see CheckUpdatePlayCoreAction
 * @see CheckUpdateFlagAction
 * @see StartUpdateAction
 * @see CancelUpdateAction
 * @see DownloadUpdateAction
 * @see com.kortisan.framework.redux.state.UpdateState
 */
sealed class UpdateActions: ReduxAction() {
    /**
     * Comprueba que hayan actualizaciones en la tienda
     * @see UpdateActions
     * @see com.kortisan.framework.redux.controllers.updatesController.UpdatesController
     */
    data object CheckUpdatePlayCoreAction: UpdateActions()

    /**
     * Comprueba la bandera que indica que hay que actualizar forzosamente
     * @see UpdateActions
     * @see com.kortisan.framework.redux.controllers.updatesController.UpdatesController
     */
    data object CheckUpdateFlagAction: UpdateActions()

    /**
     * Inicia el proceso de actualización (Instalación)
     * @see UpdateActions
     * @see com.kortisan.framework.redux.controllers.updatesController.UpdatesController
     */
    data object StartUpdateAction: UpdateActions()

    /**
     * Cancela el proceso de actualización
     * @see UpdateActions
     * @see com.kortisan.framework.redux.controllers.updatesController.UpdatesController
     */
    data object CancelUpdateAction: UpdateActions()

    /**
     * Inicia la descarga de la actualización opcional
     * @see UpdateActions
     * @see com.kortisan.framework.redux.controllers.updatesController.UpdatesController
     */
    data object DownloadUpdateAction: UpdateActions()
}
