#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}.presentation.states#end

import ${PACKAGE_NAME}.domain.models.${NAME}Model
import ${PACKAGE_NAME}.presentation.actions.${NAME}Actions
import com.kortisan.framework.redux.state.ProductionRule
import com.kortisan.framework.redux.state.ReduxState

#parse("File Header.java")
sealed class ${NAME}State : ReduxState() {
    data class CustomValue(
        val currentValue: ${NAME}Model
    ) : ${NAME}State() {
        override val productionRules = sharedProductionRules
    }

    internal val sharedProductionRules = listOf<ProductionRule>(
        { state, action ->
            if (action is ${NAME}Actions.SetCustomValue)
                CustomValue(
                    ${NAME}Model("I'am a value", 123)
                )
            else state
        },
        { state, action ->
            if (action is ${NAME}Actions.RemoveValue)
                CustomValue(
                    ${NAME}Model("No value", 0)
                )
            else state
        }
    )
}
