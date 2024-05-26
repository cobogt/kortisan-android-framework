package com.kortisan.framework.redux.controllers.updatesController

import com.kortisan.framework.redux.controllers.ReduxControllerAbstract

class UpdatesController: ReduxControllerAbstract() {
    fun cancelUpdate() {
// applicationState.updateState.value = UpdateState.DownloadCancelled
    }

    fun checkForUpdatesOnFirebase() { // Comprobamos firebase
// applicationState.updateState.value = UpdateState.CheckingUpdate
    }

    fun checkForUpdatesOnPlayCore() { // Comprobamos playcore
// applicationState.updateState.value = UpdateState.CheckingUpdate
    }

    fun startDownload() {
// applicationState.updateState.value = UpdateState.DownloadInProcess
    }

    fun startUpdateInstall() {
// applicationState.updateState.value = UpdateState.Installing
    }
}