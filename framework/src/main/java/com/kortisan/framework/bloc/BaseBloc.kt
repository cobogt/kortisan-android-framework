package com.kortisan.framework.bloc
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import androidx.compose.runtime.Composable
import com.kortisan.framework.redux.actions.ReduxAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Clase base para que todos los BLoC extiendan
 * V: ViewModel donde se encuentra el Bloc o su padre
 * B: Tipo de dato que va a representarse en el Bloc
 */
abstract class BaseBloc<B, V: ViewModelActionDispatcher>(
    open val viewModelActionDispatcher: V,
    open val state: BlocState<B>
) {
    /**
     * Este método debe llamarse en el init {} de los bloc DESPUÉS de declarar las variables
     */
    fun observeActions() {
        CoroutineScope( Dispatchers.IO ).launch {
            viewModelActionDispatcher.currentAction.collect {
                reduce( it )
            }
        }
    }

    fun dispatch( action: ReduxAction) = viewModelActionDispatcher.dispatch( action )
    open fun reduce( action: ReduxAction ) { /* Reduce una acción en los estados del BLoC */ }

    @Composable
    abstract fun Render()
}