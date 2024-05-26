package com.kortisan.framework.redux.controllers.geoposition

import com.kortisan.framework.redux.actions.GeopositionActions
import com.kortisan.framework.redux.controllers.ReduxControllerAbstract
import com.kortisan.framework.redux.controllers.ReduxControllerState
import com.kortisan.framework.redux.state.GeopositionState

class GeopositionController: ReduxControllerAbstract() {
    override fun start() {
        // Comprobamos los permisos de ubicación
        setStartStatus( ReduxControllerState.Starting )
        reduceApplicationState(
            GeopositionActions.SetGeopositionAppStateAction( GeopositionState.Loading )
        )
        TODO("Obtener posición del GPS y modificar el estado de la aplicación")
    }

}