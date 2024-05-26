package com.kortisan.framework.redux.state.validations.rules

import com.kortisan.framework.redux.state.validations.ValidationRule

/** * * * * * * * * *
 * Project DemoAppKoreFrame
 * Created by jacobo on 24/mar./2024.
 * * * * * * * * * * **/
/**
 * Valida que el texto tenga el formato de email
 */
data class ValidateEmailFormatRule(
    var value:     String,
    override val fieldName: String = "el campo"
): ValidationRule() {
    override var defaultErrorMessage: String =
        "El texto introducido ':value' en :field no corresponde con el formato de un email."

    override val placeHolderAlias: MutableMap<String, String> =
        mutableMapOf(
            "value" to value,
        )

    override fun passValidationRule(): Boolean {
        val regex = Regex("^[a-zA-Z0-9_!#\$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$")

        return value.matches( regex )
    }
}
