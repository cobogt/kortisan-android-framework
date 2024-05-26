package com.kortisan.authentication.domain

import android.app.Application
import com.kortisan.authentication.presentation.AuthenticationSceneBuilder
import com.kortisan.authentication.presentation.blocs.*
import com.kortisan.framework.bloc.BlocState
import com.kortisan.framework.bloc.ReduxViewModelBase
import com.kortisan.framework.bloc.ViewModelActionDispatcher
import com.kortisan.framework.redux.stores.ApplicationStateStore
import kotlinx.coroutines.flow.flow

class AuthenticationViewModel(
    application: Application,
    vmActionDispatcher: ViewModelActionDispatcher
): ReduxViewModelBase( application, vmActionDispatcher )  {
    private val sceneBuilder = AuthenticationSceneBuilder.Builder()

    /**
     * Hay dos caminos para controlar la navegación dentro de subflujos:
     *
     * 1) Declarar todas las posibles vistas en el ruteador del módulo y acceder a ellas
     *      mediante acciones reducidas dentro de un bloc raiz. Para pasar información entre
     *      ellos se debe extraer los parámetros de las acciones e inyectarlos en el controlador
     *      de navegación. Igual esta posibilidad debe considerarse para el sistema como un
     *      parametro en las acciones de navegación de compose o con reflexión para extraer el
     *      contenido de la clase y pasarlo al controlador de navegación.
     *
     * 2) Definir en el viewModel todos los bloc raiz de cada uno de los subflujos del modulo
     *     y posteriormente controlar que componente se muestra dentro de cada uno utilizando la
     *     reducción de estados desde dentro de cada bloc raiz. Así el contenido y la presentación
     *     de cada bloc se controlarían mediante estados y reglas de producción. Esta forma de
     *     navegación requiere que todos los subflujos sean completamente atómicos y que no
     *     dependan entre ellos a nivel de navegación.
     *
     *     Flujos del módulo: Para cada flujo es necesario crear su modelo de datos y su logica
     *          de estados.
     *
     *     Login
     *     ReadBiometrics
     */

    val loginBloc             = LoginBloc( getDispatcher(),
        BlocState(
            ApplicationStateStore, sceneBuilder.setCurrentFlow("login").build(),
            flow { /** emit( LoginModel() ) */ }
        ))

    val readBiometricsBloc    = ReadBiometricsBloc( getDispatcher(),
        BlocState(
            ApplicationStateStore, sceneBuilder.setCurrentFlow("readBiometrics").build(),
            flow { /** emit( ReadBiometricsModel() ) */ }
        ))

    val askForEmailBloc             = AskForEmailBloc( getDispatcher(),
        BlocState(
            ApplicationStateStore, sceneBuilder.setCurrentFlow("askForEmail").build(),
            flow { /** emit( AskForEmailModel() ) */ }
        ))
}