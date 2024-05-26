package com.kortisan.framework.redux.state.validations.rules

import com.kortisan.framework.redux.state.validations.ValidationRule

/** * * * * * * * * *
 * Project DemoAppKoreFrame
 * Created by jacobo on 24/mar./2024.
 * * * * * * * * * * **/
data class ValidateMinLengthRule(
    var value:     String,
    val minLength: Int = 1,
    override val fieldName: String = ""
): ValidationRule() {
    override val placeHolderAlias: MutableMap<String, String> = mutableMapOf(
        "min" to "$minLength"
    )

    override var defaultErrorMessage: String =
        "La longitud de :field es demasiado corta, debe ser de al menos :min caracteres"

    override fun passValidationRule(): Boolean {
        return value.length > minLength
    }
}
