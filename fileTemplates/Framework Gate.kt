#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}.redux.gates#end

import com.kortisan.framework.redux.actions.DefaultErrorAction
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.gates.BaseGate

#parse("File Header.java")
data object ${NAME}Gate: BaseGate() {
    override val startAction: ReduxAction = ReduxAction.EmptyAction

    override val onFailAction: ReduxAction
        get() = DefaultErrorAction("Can't continue, sorry :(")

    override fun enterInGate(startAction: ReduxAction): Boolean {
        return true
    }
}