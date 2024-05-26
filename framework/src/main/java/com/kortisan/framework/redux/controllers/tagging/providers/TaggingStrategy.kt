package com.kortisan.framework.redux.controllers.tagging.providers
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.controllers.tagging.decorators.SceneDecoratorInterface
import com.kortisan.framework.redux.controllers.remoteConfig.parsers.TaggingSettingsRemoteConfig

sealed interface TaggingStrategy {
    /**
     * Envía la información al proveedor de etiquetado
     */
    fun sendData(
        scene: SceneDecoratorInterface,
        config: TaggingSettingsRemoteConfig.TaggingEventConfig
    )

    /**
     * Inicializa al proveedor de etiquetado
     */
    fun initialize()
}