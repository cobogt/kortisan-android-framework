package com.kortisan.framework.redux.state.validations.rules

import com.kortisan.framework.redux.state.validations.ValidationRule

/** * * * * * * * * *
 * Project DemoAppKoreFrame
 * Created by jacobo on 24/mar./2024.
 * * * * * * * * * * **/
data class ValidateNotEmptyRule(
    var value:     String,
    override val fieldName: String = "",
): ValidationRule() {
    override var defaultErrorMessage: String =
        "El campo :field no puede ir vacío"

    override fun passValidationRule(): Boolean {
        return value.isNotBlank() || value.isNotEmpty()
    }
}
