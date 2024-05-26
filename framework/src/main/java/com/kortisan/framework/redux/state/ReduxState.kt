package com.kortisan.framework.redux.state

import com.kortisan.framework.redux.actions.LoadStateAction
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.state.caretaker.CaretakerStrategy
import com.kortisan.framework.redux.state.productionrules.ProductionRule

/**
 * Estado aplicable a la arquitectura Redux
 *
 * Ejemplo:
 *
 * class RRR: ReduxState() {
 *   override val productionRules: List<ProductionRule> =
 *   listOf ({ state, action ->
 *     when( action ) {
 *       is ReduxAction.EmptyAction -> MMM()
 *       else -> state
 *     }
 *   }, {
 *   state, action ->
 *     when( action ) {
 *       is ReduxAction.EmptyAction -> OOO()
 *       else -> state
 *     }
 *   })
 *
 *   fun algo() {
 *     var w: ReduxState = MMM()
 *     w = w.reduce( ReduxAction.emptyAction )
 *   }
 * }
 *
 * class MMM: ReduxState()
 * class OOO: ReduxState()
 */
abstract class ReduxState {
    /**
     * Si no hay reglas de producción se considera como un estado final
     */
    open val productionRules = listOf<ProductionRule>()

    /**
     * Estrategia para almacenar y recuperar el estado actual después de una reducción
     */
    open val caretakerStrategy: CaretakerStrategy? = null

    /**
     * Reduce el estado actual a partir de una acción y devuelve el nuevo estado en caso de que
     * haya una regla de producción que aplique.
     */
    inline fun <reified T: ReduxState>reduce( action: ReduxAction ): T =
        productionRules
            .let {
                // En caso de que aplique reducimos el estado a partir del caretaker
                caretakerStrategy?.productionRule?.let { caretakerProductionRule ->
                    it.plus( caretakerProductionRule )
                } ?: it
            }
            // Ejecutamos las reglas de producción
            .map { it.invoke( this, action ) as T }
            // Filtramos los estados producidos que sean diferentes al actual y escogemos el primero
            .firstOrNull { this != it }
            ?.also {
                if( action !is LoadStateAction )
                    caretakerStrategy?.persist( it as ReduxState )
            } ?: this as T
}
