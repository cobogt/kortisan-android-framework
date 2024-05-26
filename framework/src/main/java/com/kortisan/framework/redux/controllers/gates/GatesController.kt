package com.kortisan.framework.redux.controllers.gates
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.util.Log
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.ReduxControllerAbstract
import com.kortisan.framework.bloc.ViewModelActionDispatcher
import com.kortisan.framework.redux.ApplicationActionDispatcher
import com.kortisan.framework.redux.actions.ExitGateAction
import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.controllers.ControllersProxy
import com.kortisan.framework.redux.controllers.navigation.NavigationController
import com.kortisan.framework.redux.controllers.navigation.strategies.NavigationStrategy
import java.util.Stack

/**
 * Esta clase se encarga de controlar la pila de acciones, sus dependencias o requisitos así como
 * la salida de esta pila con su respectiva respuestq.
 */
class GatesController: ReduxControllerAbstract() {
    /**
     * Pila de flujos de navegación (con su pila de estados dentro)
     */
    private val actionCourseStack: Stack<ActionCourse> = Stack()

    /**
     * Entrada a una puerta: Evalua una puerta y determina la acción resultante de forma recursiva
     */
    fun evalGate(
        gate:           BaseGate,
        originalAction: ReduxAction,
        dispatcher:     ViewModelActionDispatcher? = null
    ): ReduxAction {
        var replaceAction = originalAction

        // Si todas las dependencias del gate se cumplen, procedemos a evaluar el gate actual
        if( gate.enterInGate( originalAction )) {
            // Es necesario reemplazar la acción original por la inicial de éste gate
            var gateExistsInStack = false

            actionCourseStack.forEach {
                if( it.gate.name == gate.name )
                    gateExistsInStack = true
            }

            replaceAction = gate.startAction

            // La acción fue reemplazada y es una acción de navegación
            if( replaceAction != originalAction )
                // Comprobamos que la configuración del gate permita repetirse en la pila
                if( (gate.allowRepeatOnStack && gateExistsInStack) || ! gateExistsInStack )
                    actionCourseStack.push(
                        ActionCourse(
                            originalAction,
                            gate,
                            dispatcher,
                            // Asociamos la actividad al nuevo elemento de la pila de flujos.
                            getCurrentLaunchActivityName( replaceAction )
                        )
                    )
                else
                    // No se permite repetir la entrada a la puerta
                    replaceAction = ReduxAction.EmptyAction
        }

        /**
         * Evaluamos todas las sub puertas y obtenemos la primera cuya evaluación implique
         * un cambio en la acción objetivo.
         * No se permite la incepción, o un gate que se llama a sí mismo ni gates ciclicos
         * en las dependencias, para eso existe el resultado tipo retry.
         */
        gate.subGates.filterNot {
            it.name == gate.name && it.subGates.contains( gate )
        }.forEach {
            // Si es necesario entrar a una sub puerta la evaluamos recursivamente
            // Estas son las dependencias de este gate, por tanto se evaluan antes del gate.
            if( it.enterInGate( originalAction ))
                replaceAction = evalGate(
                    gate = it,
                    originalAction = replaceAction,
                    dispatcher
                )
        }

        return replaceAction
    }

    /**
     * Evaluamos si la acción de reemplazo produce una nueva actividad para abstraer el
     * flujo. Esto se utiliza para las salidas de subflujos ya que permite controlar cuando
     * una respuesta debe cerrar la actividad actual para continuar con la anterior
     */
    private fun getCurrentLaunchActivityName( action: ReduxAction ): String? =
        NavigationStrategy.currentActivity?.javaClass?.canonicalName?.let { activityName ->
            if( action is NavigationActions ) {
                val activityClassNameLaunched: String = action.let {
                    ControllersProxy.getController<NavigationController>()
                        ?.getClassNameForNavigationAction( it ) ?: ""
                }

                // Existe una actividad y es diferente de la que se va a lanzar
                if( activityName != activityClassNameLaunched )
                    activityClassNameLaunched
                else
                    activityName
            } else
                activityName
        }

    /**
     * Salida de una puerta
     *
     * Extraemos un elemento de la pila de flujos de navegación y navegamos al
     * último elemento de la pila
     */
    fun <R>exitSubFlow( exitGateResult: ExitGateResult<R> ): ReduxAction {
        if( actionCourseStack.isEmpty() )
            return ReduxAction.EmptyAction

        val currentActionCourse = actionCourseStack.pop()

        // Comprobamos que la actividad dònde se encuentra el subflujo no sea necesaria para
        // otros subflujos anteriores
        Log.d("GATES CONTROLLER stack", actionCourseStack.map { it.activityClassName }.toString() )
        Log.d("GATES CONTROLLER", currentActionCourse.toString() )
        Log.d("GATES CONTROLLER", "${NavigationStrategy.currentActivity}" )
        if ( actionCourseStack.isEmpty() ) {
            NavigationStrategy.currentActivity?.run {
                // Si la actividad es diferente a la que la lanzó
                if( javaClass.canonicalName != currentActionCourse.activityClassName ) {
                    // TODO: Si la actividad se va a cerrar se debe agregar la respuesta de la
                    // acción a la respuesta de la actividad, asumiendo que fue iniciada en la
                    // espera de un resultado.
                    currentActionCourse.dispatcher = null
                }
                finish()
            }
        }

        // ¿A dónde se va a mandar la acción al salir del subflujo?
        val dispatcher = currentActionCourse.dispatcher
            ?: ApplicationActionDispatcher.getInstance()

        // ¿Qué acción se va a mandar al salir del subflujo?
        val resultAction = when( exitGateResult ) {
            is ExitGateResult.Retry ->
                if( currentActionCourse.gate.retryAllowed )
                    exitGateResult.customResultRetryAction
                        ?: currentActionCourse.gate.startAction
                else
                    currentActionCourse.gate.onFailAction
            is ExitGateResult.Fail ->
                exitGateResult.customResultFailAction
                    ?: currentActionCourse.gate.onFailAction
            is ExitGateResult.Success -> {
                // Si hay una acción personalizada de salida la enviamos al despachador
                exitGateResult.customResultSuccessAction?.also {
                    dispatcher.dispatch( it )
                }

                currentActionCourse.gate.onSuccessAction
                    ?: currentActionCourse.originalAction
            }
        }

        // Al entrar al subflujo se inició una nueva actividad? Entonces se debe
        // terminar la actividad si no hay dependencias en los subflujos de la pila.

        // Una salida de error debe poder vaciar la pila de subflujos que lo tienen como
        // dependencia.

        // En caso de que aplique se lanza la acción de última voluntad
        currentActionCourse.gate.lastWillAction?.also { lastWillAction ->
            dispatcher.dispatch(
                lastWillAction
            )
        }

        // En caso de que el subflujo emita datos, estos se pueden recolectar en el despachador
        exitGateResult.rawDataResult?.also { rawDataResult ->
            dispatcher.dispatch(
                ExitGateAction.SetRawDataResultAction(
                    currentActionCourse.gate,
                    currentActionCourse.originalAction,
                    resultAction,
                    rawDataResult
                )
            )
        }

        return resultAction
    }

    private data class ActionCourse(
        val originalAction:    ReduxAction,
        val gate:              BaseGate,
        var dispatcher:        ViewModelActionDispatcher? = null,
        val activityClassName: String? = null
    )
}