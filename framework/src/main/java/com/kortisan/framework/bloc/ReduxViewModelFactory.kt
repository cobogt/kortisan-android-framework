package com.kortisan.framework.bloc
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
 * * * * * * * * * * **/

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.kortisan.framework.redux.ApplicationActionDispatcher
import com.kortisan.framework.redux.state.NavigationState
import com.kortisan.framework.redux.stores.ApplicationStateStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * Esta clase es inyectada al momento de crear una instancia de un viewModel y es
 * la encargada de conectar al viewModel con el dispatcher general de la aplicación
 * para evitar recibir llamadas de acción de otros viewModel
 *
 * El ACTIVITY en esta clase es para filtrar todas las acciones que le corresponden
 * a esta actividad
 */
object ReduxViewModelFactory:
    ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val application = checkNotNull( extras[ APPLICATION_KEY ] )
        val dispatcher = ApplicationActionDispatcher.getInstance(
            // Al momento de instanciar el viewModel, se obtiene la acción que desencadenó la
            // creación de la vista y se continúa dentro del viewModel, para poder gestionar el
            // ruteo dentro del nuevo viewModel.
            runBlocking {
                ApplicationStateStore.navigationState.first().let {

                    if( it is NavigationState.NavigateToActivity ) {
                        it.actionDispatched
                    } else null
                }
            }
        )

        return modelClass.getConstructor(
            Application::class.java,
            ViewModelActionDispatcher::class.java
        ).newInstance(
            application,
            dispatcher
        ) as T
    }
}
