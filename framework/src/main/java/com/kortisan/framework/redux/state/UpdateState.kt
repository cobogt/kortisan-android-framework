package com.kortisan.framework.redux.state

/**
 * Gestiona el estado de las actualizaciones de la aplicación
 *
 * @see NoUpdate
 * @see FlexibleUpdatePending
 * @see MandatoryUpdatePending
 * @see DownloadInProcess
 * @see DownloadFinished
 * @see DownloadCancelled
 * @see InstallFinished
 * @see InstallFailed
 * @see CheckingUpdate
 * @see Installing
 * @see UpdateSkipped
 *
 * @see com.kortisan.framework.redux.controllers.updatesController.UpdatesController
 * @see com.kortisan.framework.redux.actions.UpdateActions
 */
sealed class UpdateState: ReduxState() {
    /**
     * No hay actualizaciones pendientes.
     * - Estado inicial y predeterminado
     * @see UpdateState
     */
    data object NoUpdate:               UpdateState()

    /**
     * Actualización opcional disponible.
     * @see UpdateState
     */
    data object FlexibleUpdatePending:  UpdateState()

    /**
     * Actualización obligatoria pendiente, no se debe continuar a la aplicación hasta actualizar.
     * @see UpdateState
     */
    data object MandatoryUpdatePending: UpdateState()

    /**
     * Descarga de una actualización en proceso.
     * @see UpdateState
     */
    data object DownloadInProcess:      UpdateState()

    /**
     * Descarga de la actualización en terminada.
     * @see UpdateState
     */
    data object DownloadFinished:       UpdateState()

    /**
     * Descarga de la actualización cancelada.
     * @see UpdateState
     */
    data object DownloadCancelled:      UpdateState()

    /**
     * Instalación de la actualización finalizada.
     * @see UpdateState
     */
    data object InstallFinished:        UpdateState()

    /**
     * Error al instalar la actualización.
     * - Se debe recomendar reinstalar en caso de que sea una actualización obligatoria.
     * @see UpdateState
     */
    data object InstallFailed:          UpdateState()

    /**
     * Comprobando actualizaciones.
     * @see UpdateState
     */
    data object CheckingUpdate:         UpdateState()

    /**
     * Instalando actualización.
     * @see UpdateState
     */
    data object Installing:             UpdateState()

    /**
     * Actualización opcional omitida.
     * @see UpdateState
     */
    data object UpdateSkipped:          UpdateState()
}