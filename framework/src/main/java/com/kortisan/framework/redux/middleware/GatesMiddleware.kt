package com.kortisan.framework.redux.middleware
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.ExitGateAction
import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.ControllersProxy
import com.kortisan.framework.redux.controllers.gates.GatesController
import com.kortisan.framework.bloc.ViewModelActionDispatcher
import com.kortisan.framework.redux.stores.ApplicationStateStore

/**
 * Este middleware se encarga de el reemplzo de las acciones para gestionar la entrada y salida de
 * un subflujo a partir de los gates de una acción.
 * En el despachador se continuan las acciones que emergen de un subflujo como producto de su
 * resultado o efectos propios.
 *
 * Una aplicación sin subflujos es completamente válida, sin embargo se vuelve lineal.
 */
class GatesMiddleware(
    private val dispatcher: ViewModelActionDispatcher? = null
): MiddlewareInterface {
    override fun doNext(action: ReduxAction, applicationState: ApplicationStateStore): ReduxAction {
        var evaluatedAction = action

        ControllersProxy.getController<GatesController>()?.run {
            // Si es una acción de salida de flujo la pasamos directo
            if( action is ExitGateAction<*> )
                evaluatedAction = exitSubFlow( action.result )
            else {
                var gates = action.gates

                // Para obtener las puertas asociadas a un objetivo de navegación
                if( action is NavigationActions.NavigateToTarget )
                    action.target.accessGate?.also {
                        gates = gates.plus( it )
                    }


                // Obtenemos las gates de la acción
                gates.firstOrNull { gate ->
                    // Identificamos la primer gate que no pudo ser procesada
                    evaluatedAction = evalGate( gate, action, dispatcher )

                    evaluatedAction != action
                }
            }
        }

        return evaluatedAction
    }
}