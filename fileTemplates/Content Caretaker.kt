#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}.state.caretakers#end

import com.kortisan.framework.redux.state.caretaker.CaretakerStrategy
import com.kortisan.framework.redux.state.ReduxState
import com.kortisan.framework.storage.security.NoSecurityStrategy
import com.kortisan.framework.storage.security.SecurityStrategy

#parse("File Header.java")
class ${NAME}CaretakerStrategy(
    override val defaultState: ${NAME}State
): CaretakerStrategy() {
    // Put the State data into a storage
    override fun persist(currentState: ${NAME}State) {
        TODO("Not yet implemented")
    }

    // Get the data from a storage and returns a State
    override fun recover(stateClassName: String): ${NAME}State? {
        TODO("Not yet implemented")
    }
    
    // How to protect the data
    override val securityStrategy: SecurityStrategy = NoSecurityStrategy
}