package com.kortisan.framework.redux.controllers.tagging.providers
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.kortisan.framework.FrameworkApplicationBinding
import com.kortisan.framework.redux.controllers.tagging.decorators.SceneDecoratorInterface
import com.kortisan.framework.redux.stores.ApplicationStateStore

import com.kortisan.framework.redux.controllers.remoteConfig.parsers.TaggingSettingsRemoteConfig
import com.kortisan.framework.toBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

data object FirebaseTaggingStrategy: TaggingStrategy {
    private val TAG = javaClass.simpleName
    private var initialized: Boolean = false

    override fun sendData(
        scene: SceneDecoratorInterface,
        config: TaggingSettingsRemoteConfig.TaggingEventConfig
    ) {
        if( ! initialized ) {
            Log.w(TAG, "No se ha inicializado el proveedor")
            return
        }

        // Obtenemos la información de la escena
        val taggingData       = scene.getData()
        // Aplicamos los filtros de la configuración
        val filteredData      = config.filterData( taggingData )
        // Ajustamos el esquema de registro para el etiquetado
        val eventName: String = config.logScheme ?: taggingData["eventName"] ?: ""

        // Obtenemos la instancia de Firebase para la aplicación
        FirebaseAnalytics.getInstance(
            FrameworkApplicationBinding.appContext
        ).run {
            // Registramos el evento en el proveedor con la información recopilada
            logEvent( eventName, filteredData.toBundle() )
        }
    }

    override fun initialize() {
        if( ! initialized )
            CoroutineScope( Dispatchers.Default ).launch {
                ApplicationStateStore.sessionState.collect { _ ->
                    initialized = true

                    FirebaseAnalytics.getInstance(
                        FrameworkApplicationBinding.appContext
                    ).run {
                        setUserId( UUID.randomUUID().toString() )
                    }
                }
            }
    }
}