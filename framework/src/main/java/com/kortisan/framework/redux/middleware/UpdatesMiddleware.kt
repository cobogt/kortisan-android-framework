package com.kortisan.framework.redux.middleware
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.actions.UpdateActions
import com.kortisan.framework.redux.controllers.ControllersProxy
import com.kortisan.framework.redux.controllers.updatesController.UpdatesController
import com.kortisan.framework.redux.stores.ApplicationStateStore

/**
 * Gestiona la actualización de la aplicación mediante el Controlador de actualizaciones
 */
object UpdatesMiddleware: MiddlewareInterface {
    override fun doNext(action: ReduxAction, applicationState: ApplicationStateStore): ReduxAction =
        if( action is UpdateActions) {
            val controller = ControllersProxy.getController<UpdatesController>()

            when( action ) {
                UpdateActions.CancelUpdateAction ->
                    controller?.cancelUpdate()

                UpdateActions.CheckUpdateFlagAction ->
                    controller?.checkForUpdatesOnFirebase()

                UpdateActions.CheckUpdatePlayCoreAction ->
                    controller?.checkForUpdatesOnPlayCore()

                UpdateActions.DownloadUpdateAction ->
                    controller?.startDownload()

                UpdateActions.StartUpdateAction ->
                    controller?.startUpdateInstall()
            }

            ReduxAction.EmptyAction
        } else
            action
}