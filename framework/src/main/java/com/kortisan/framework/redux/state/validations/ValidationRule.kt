package com.kortisan.framework.redux.state.validations

import com.kortisan.framework.FrameworkApplicationBinding

/** * * * * * * * * *
 * Project DemoAppKoreFrame
 * Created by jacobo on 24/mar./2024.
 * * * * * * * * * * **/

/**
 * Regla de validación para un estado
 */
abstract class ValidationRule() {
    /**
     * Plantilla del sistema para el mensaje de error (Opcional)
     */
    open val templateResource: Int? = null

    /**
     * Mensaje predeterminado para el error, debe ser sobreescrito.
     */
    abstract var defaultErrorMessage: String

    /**
     * Permite reemplazar un alias del mensaje de error marcado con la plantilla
     * :cualquierAlias con un valor definido al momento de instanciar la regla.
     */
    open val placeHolderAlias: MutableMap<String, String> = mutableMapOf()

    /**
     * Función principal que evalua una regla, debe ser sobreescrita.
     * Verdadero para pasar, falso para devolver un error.
     */
    abstract fun passValidationRule(): Boolean

    /**
     * Nombre del campo en donde aplica la regla
     */
    abstract val fieldName: String

    /**
     * Lista de validaciones dependientes que aplican de forma recursiva
     * TODO: Hacer esta función recursiva
     */
    open val dependentValidationRules: MutableList<ValidationRule> = mutableListOf()
}

/**
 * Lista los resultados de validar el arreglo de reglas
 */
fun List<ValidationRule>.evaluate(): StateValidationResult {
    // Lista con los mensajes de error
    val validationErrors: MutableList<String> = mutableListOf()

    // Evalua las reglas de validación definidas en un estado Validable
    // También reemplaza los placeHolderAlias para el mensaje de error
    forEach { validationRule ->
        if( ! validationRule.placeHolderAlias.contains("field") )
            validationRule.placeHolderAlias.put(
                "field",
                validationRule.fieldName
            )

        validationRule.passValidationRule().also {
            if( !it) {
                val message = (validationRule.templateResource?.let {
                    FrameworkApplicationBinding.appContext.getString(it) }
                    ?: validationRule.defaultErrorMessage)
                    .let { message ->
                        var newMessage = message

                        validationRule.placeHolderAlias.forEach { alias ->
                            newMessage = newMessage.replace(":" + alias.key, alias.value)
                        }

                        newMessage
                    }

                validationErrors.add( message )
            }
        }
    }

    return if( validationErrors.isEmpty() )
        StateValidationResult.SuccessResult
    else
        StateValidationResult.ErrorResult(validationErrors)
}