package com.kortisan.framework.bloc
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.controllers.tagging.decorators.SceneDecoratorInterface
import com.kortisan.framework.redux.stores.ApplicationStateStore
import kotlinx.coroutines.flow.Flow

data class BlocState<T> (
    val applicationState: ApplicationStateStore,
    val scene: SceneDecoratorInterface,
    // Los eventos deben estar en el estado del componente
    val componentState:   Flow<T>,
)