package com.kortisan.framework.redux.state.validations

/** * * * * * * * * *
 * Project DemoAppKoreFrame
 * Created by jacobo on 24/mar./2024.
 * * * * * * * * * * **/

/**
 * Wrapper para los resultados de la validación de un estado.
 */
sealed class StateValidationResult {
    /**
     * Validación con resultado exitoso
     */
    data object SuccessResult: StateValidationResult()

    /**
     * Validación con errores
     */
    data class ErrorResult(
        val errorList: List<String>
    ): StateValidationResult()
}
