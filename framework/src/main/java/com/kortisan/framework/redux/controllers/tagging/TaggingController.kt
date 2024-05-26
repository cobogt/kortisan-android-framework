package com.kortisan.framework.redux.controllers.tagging

import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.ReduxControllerAbstract
import com.kortisan.framework.redux.controllers.ReduxControllerState
import com.kortisan.framework.redux.controllers.remoteConfig.parsers.TaggingSettingsRemoteConfig
import com.kortisan.framework.redux.controllers.tagging.decorators.SceneDecoratorInterface
import com.kortisan.framework.redux.controllers.tagging.decorators.application.SensorsTaggingDecorator
import com.kortisan.framework.redux.controllers.tagging.providers.FirebaseTaggingStrategy
import com.kortisan.framework.redux.state.RemoteConfigState
import com.kortisan.framework.redux.stores.ApplicationStateStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 06/01/23.
 * * * * * * * * * * **/
class TaggingController: ReduxControllerAbstract() {
    private var taggingSettingsRemoteConfig = TaggingSettingsRemoteConfig()

    override fun start() {
        RemoteConfigState.getContent<TaggingSettingsRemoteConfig>("tagingSettings")?.also {
            taggingSettingsRemoteConfig = it

            // Inicializamos a los proveedores
            FirebaseTaggingStrategy.initialize()

            setStartStatus( ReduxControllerState.Started )
        }
    }

    fun launchTagging( action: ReduxAction ) {
        // Mandamos el evento de etiquetado en una corutina para evitar lags
        CoroutineScope( Dispatchers.IO ).launch {
            action.tagging
                ?.let {
                    var decoratedScene: SceneDecoratorInterface = it

                    // Aquí aplica los decoradores de nivel aplicación
                    decoratedScene = SensorsTaggingDecorator( decoratedScene )
                        .setSensorsState( ApplicationStateStore.sensorsState.first() )

                    decoratedScene
                }?.also { scene ->
                taggingSettingsRemoteConfig.firebaseConfig.also { firebaseRemoteConfig ->
                    FirebaseTaggingStrategy.sendData( scene, firebaseRemoteConfig )
                }
            }
        }
    }
}