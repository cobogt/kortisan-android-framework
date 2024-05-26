#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}.presentation.actions#end

import com.kortisan.framework.redux.actions.ReduxAction

#parse("File Header.java")
sealed class ${NAME}Actions: ReduxAction() {
    data class SetCustomValue( val text: String, val number: Int ): ${NAME}Actions()
    data object RemoveValue: ${NAME}Actions()
}