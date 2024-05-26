package com.kortisan.framework.redux.middleware
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.controllers.navigation.strategies.NavigationStrategy
import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.ControllersProxy
import com.kortisan.framework.redux.controllers.navigation.NavigationController
import com.kortisan.framework.redux.stores.ApplicationStateStore

object NavigationMiddleware: MiddlewareInterface {
    override fun doNext(action: ReduxAction, applicationState: ApplicationStateStore): ReduxAction {
        /**
        * El estado de la navegación debe ser persistente en local, los parámetros se deben guardar
        *   en la base de datos así como la pila de actividades.
        * El estado debe poder ser retomado aun cuando se cierre la aplicación.
        * Manejar las pilas y la solicitudes de respuesta (activityResult) en la persistencia local
        *   prermitirá tener una autogestión de las respuestas  así como reinyectarlas en el
        *   dispatcher de acciones (Revisar si hay algo que haga esto mismo de manera nativa)
        * */
        if( action is NavigationActions ) {
            val navigationController = ControllersProxy.getController<NavigationController>(
                action
            )

            // Continuamos la acción en el estado de la aplicación para poder identificar
            // la ruta actual en la que se encuentra el usuario.
            navigationController?.reduceApplicationState( action )

            when( action ) {
                is NavigationActions.NavigateToTarget ->
                    navigationController?.navigateToTarget(
                        action.target
                    )
                is NavigationActions.NavigateToClass ->
                    NavigationStrategy.navigateToActivityClassWithoutResult(
                        action.className,
                        action.params
                    )
                is NavigationActions.NavigateToRoute ->
                    navigationController?.navigateToRoute(
                        action.route,
                        action.params
                    )
                is NavigationActions.NavigateToTargetWithParams ->
                    navigationController?.navigateToTargetWithParams(
                        action.target,
                        action.params
                    )
            }
        }
        /**
         * Caso 1: Abrir una actividad OK
         * Caso 2: Abrir una actividad con parámetros en el intent OK
         * Caso 3: Abrir una actividad e inyectar el viewmodel OK
         * Caso 4: Abrir una actividad preparada para recibir un navigationController y un viewmodel OK
         * Caso 5: Abrir una actividad esperando una respuesta
        */
        return action
    }
}