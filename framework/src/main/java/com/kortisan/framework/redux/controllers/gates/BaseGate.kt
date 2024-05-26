package com.kortisan.framework.redux.controllers.gates
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.ReduxAction

/**
 * Una puerta es una condición que se ejecuta de forma recursiva, evalua el estado interno o externo
 * de la aplicación (WebService) para determinar si se procede con la acción inicial.
 * En caso de no proceder, la acción original será reemplazada por la acción en caso de falla de la
 * puerta.
 * Si está definida, la acción en caso de exito reemplazará a la acción original, esto sólo aplica
 * con la primer puerta invocada en la serie recursiva.
 *
 * Adicionalmente, se invocará la acción de última voluntad en caso de estar definida para todas las
 * puertas de forma recursiva, esto a através del dispatcher principal de la aplicación.
 */
abstract class BaseGate {
    // Dependencias a comprobar antes de la propia Gate
    open val subGates:           List<BaseGate> = listOf()

    // Nombre que va a tener el subflujo de navegación en la pila en caso de una redirección.
    val name:                    String = javaClass.simpleName

    // Acción inicial en caso de entrar al Gate.
    abstract val startAction:    ReduxAction

    // Acción a ejecutar en caso de que la salida del Gate sea fallida.
    abstract val onFailAction:   ReduxAction

    // Acción para sustituir la acción original en caso de éxito.
    open val onSuccessAction:    ReduxAction? = null

    // Indica si una puerta puede volver a lanzarse en caso de falla.
    open val retryAllowed:       Boolean = false

    // Permite repetir la misma acción varias veces en la pila.
    open val allowRepeatOnStack: Boolean = false

    // Acción que se envía en todos los casos de salida excepto en el abort.
    open val lastWillAction:     ReduxAction? = null

    /**
     * Condición a evaluar para entrar a la puerta y agregar un elemento a la pila de flujos
     * Un valor FALSO significa que pasó la condición y contiunua al siguiente gate.
     * Un valor VERDADERO significa que hay que crear un subflujo de navegación.
     */
    open fun enterInGate( startAction: ReduxAction ): Boolean = false
}