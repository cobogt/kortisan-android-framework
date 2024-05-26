package com.kortisan.authentication.presentation.states

import com.kortisan.authentication.presentation.states.productionRules.AskForEmailProductionRules
import com.kortisan.framework.redux.state.productionrules.ProductionRule
import com.kortisan.framework.redux.state.ReduxState

/**
 * Estado que almacena el Email y valida que sea correcto
 * Usa la validación de longitud, formato y que no sea vacío.
 */
sealed class AskForEmailState: ReduxState() {
    /**
     * En este caso particular se comparten las mismas reglas de producción
     */
    protected val sharedProductionRules = AskForEmailProductionRules.sharedProductionRules

    data object EmptyInput: AskForEmailState() {
        override val productionRules: List<ProductionRule> = sharedProductionRules
    }

    data class ErrorOnInput(
        val currentValue: String,
        val errorMessage: String
    ): AskForEmailState() {
        override val productionRules: List<ProductionRule> = sharedProductionRules
    }

    data class SuccessInput(
        val currentValue: String
    ): AskForEmailState() {
        override val productionRules: List<ProductionRule> = sharedProductionRules
    }
}
