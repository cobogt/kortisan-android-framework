package com.kortisan.framework.redux.state.validations

import com.kortisan.framework.redux.state.ReduxState
import com.kortisan.framework.redux.state.validations.rules.ValidateEmailFormatRule
import com.kortisan.framework.redux.state.validations.rules.ValidateMinLengthRule
import com.kortisan.framework.redux.state.validations.rules.ValidateNotEmptyRule
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

/** * * * * * * * * *
 * Project DemoAppKoreFrame
 * Created by jacobo on 24/mar./2024.
 * * * * * * * * * * **/
class ValidationBuilder private constructor() {
    // NombreCampo to Pair<Tipo, Valor en String>
    protected val valueMap: MutableMap<String, Pair<String, String>> = mutableMapOf()
    protected val validationRules: MutableList<ValidationRule> = mutableListOf()
    protected val availableRules: Map<String, KClass<*>> = mapOf(
        "email"     to ValidateEmailFormatRule::class,
        "min"       to ValidateMinLengthRule::class,
        "required"  to ValidateNotEmptyRule::class,
    )

    inner class Builder(
        private val stateSource: ReduxState
    ) {
        private val validationBuilder = ValidationBuilder()

        init {
            // Extrae los valores del estado fuente
            validationBuilder.valueMap.putAll( extractStateValues(stateSource) )
        }

        fun build(): ValidationBuilder {
            return validationBuilder
        }

        fun addValidations( validations: Map<String, String> ) = apply {
            validations.forEach {
                addValidation( it.key, it.value )
            }
        }

        fun addValidation( field: String, stringRule: String ) = apply {
            parseStringRule( field, stringRule )?.also { addRule(it) }
        }

        fun addRule( validationRule: ValidationRule ) = apply {
            validationBuilder.validationRules.add( validationRule )
        }

        /**
         * Devuelve una instancia para una regla de validación en formato de texto
         */
        private fun parseStringRule( field: String, rules: String ): ValidationRule? {
            // Separamos las reglas con un Pipe
/*            rules.split("|").forEach { rule ->
                val (ruleName, rulePramsString) = rule.split(":")

                val ruleParams = rulePramsString.split(",")
                val paramsForInstance: MutableMap<Any, Any?> = mutableMapOf(
                    "fieldName" to field
                )
                val primaryConstructorParamsName = mutableListOf<String>()

                val instance = availableRules.get(ruleName)
                    ?.primaryConstructor?.also {
                        it.parameters.filter { !it.isOptional }.forEach { p ->
                            p.name?.also{ pName ->
                                primaryConstructorParamsName.add(pName)
                            }
                        }
                    }
                    ?.callBy(
                        primaryConstructorParamsName.associateBy(
                            {it},
                        )
                    ) as ValidationRule?

                return instance
            }*/

            return null
        }

        /**
         * Usa la reflexión para extraer los valores de un estado
         */
        private fun extractStateValues( stateSource: ReduxState ) =
            mutableMapOf<String, Pair<String, String>>()
    }
}