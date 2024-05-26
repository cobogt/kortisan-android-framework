package com.kortisan.authentication.presentation.states.productionRules

import com.kortisan.authentication.presentation.actions.SetTextAction
import com.kortisan.authentication.presentation.states.AskForEmailState
import com.kortisan.framework.redux.state.productionrules.ProductionRule
import com.kortisan.framework.redux.state.validations.StateValidationResult
import com.kortisan.framework.redux.state.validations.ValidationRule
import com.kortisan.framework.redux.state.validations.evaluate
import com.kortisan.framework.redux.state.validations.rules.ValidateEmailFormatRule
import com.kortisan.framework.redux.state.validations.rules.ValidateMinLengthRule
import com.kortisan.framework.redux.state.validations.rules.ValidateNotEmptyRule

/** * * * * * * * * *
 * Project DemoAppKoreFrame
 * Created by jacobo on 24/mar./2024.
 * * * * * * * * * * **/
data object AskForEmailProductionRules {
    val sharedProductionRules = listOf<ProductionRule> (
        { state, action ->
            if ( state !is AskForEmailState.EmptyInput && action is SetTextAction) {
                // Ejecutamos las reglas de validación
                val validationRules: List<ValidationRule> = listOf(
                    ValidateMinLengthRule(action.newText, 5, "Email"),
                    ValidateEmailFormatRule(action.newText, "Email"),
                    ValidateNotEmptyRule(action.newText, "Email")
                )

                val validationResult = validationRules.evaluate()
                when( validationResult ) {
                    is StateValidationResult.SuccessResult ->
                        AskForEmailState.SuccessInput(action.newText)

                    is StateValidationResult.ErrorResult ->
                        if( action.newText.isEmpty() )
                            AskForEmailState.EmptyInput
                        else
                            AskForEmailState.ErrorOnInput(
                                action.newText,
                                validationResult.errorList.joinToString(", ")
                            )
                }
            }else
                state
        }, { state, action ->
            if( state is AskForEmailState.EmptyInput && action is SetTextAction) {
                val validationRules: List<ValidationRule> = listOf(
                    ValidateMinLengthRule(action.newText, 5, "Email"),
                    ValidateEmailFormatRule(action.newText, "Email"),
                    ValidateNotEmptyRule(action.newText, "Email")
                )

                val validationResult = validationRules.evaluate()

                when ( validationResult ) {
                    is StateValidationResult.ErrorResult ->
                        AskForEmailState.ErrorOnInput(
                            action.newText,
                            validationResult.errorList.joinToString(", ")
                        )
                    is StateValidationResult.SuccessResult ->
                        AskForEmailState.SuccessInput(action.newText)
                }
            }else
                state
        }
    )
}