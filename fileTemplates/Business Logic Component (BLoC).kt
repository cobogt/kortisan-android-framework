#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}.presentation.blocs#end

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.kortisan.framework.bloc.BlocState
import com.kortisan.framework.bloc.ViewModelActionDispatcher
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.bloc.BaseBloc

// Component
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")import ${PACKAGE_NAME}.presentation.components.Custom${NAME}Component#end

// BLoC Model
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")import ${PACKAGE_NAME}.domain.models.${NAME}Model#end

// BLoC State
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")import ${PACKAGE_NAME}.presentation.states.${NAME}State#end

// BLoC Actions
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")import ${PACKAGE_NAME}.presentation.actions.${NAME}Actions#end

#parse("File Header.java")
class ${NAME}Bloc(
    override val viewModelActionDispatcher: ViewModelActionDispatcher,
    override val state: BlocState<${NAME}Model>
): BaseBloc<${NAME}Model, ViewModelActionDispatcher>(
    viewModelActionDispatcher, state
) {
    private val current${NAME}State = MutableStateFlow<${NAME}State>(
        ${NAME}State.CustomValue(
            ${NAME}Model("Init", 1)
        )
    )

    // This should be under states
    init { observeActions() }

    override fun reduce(action: ReduxAction) {
        // Reducer
        current${NAME}State.value = current${NAME}State.value.reduce( action )
    }

    @Composable
    override fun Render() {
        // Integrate the BLoC bus state into the internal BLoC state
        val coroutine = rememberCoroutineScope()
        LaunchedEffect(key1 = Unit) {
            coroutine.launch { 
                state.componentState.collect {
                    // Default State, BLoC can contain State as Model
                    current${NAME}State.value = ${NAME}State.CustomValue( it )
                }
            }
        }
        
        val current${NAME}State by state.componentState.collectAsState(
            initial = ${NAME}Model("", 0)
        )

        Column {
            Row {
                Text(text = "Hello, my name is ${NAME}")
                Text(text = "BLoC Value")
                Text(text = "Internal value:" + current${NAME}State.demoText)
                Text(text = "Internal value:" + current${NAME}State.demoInt)
                Custom${NAME}Component( "Hello world!" )
            }
            
            Row {
                Button(onClick = { 
                    viewModelActionDispatcher.dispatch(
                        ${NAME}Actions.SetCustomValue("CUSTOM", 999)
                    )
                }) {
                    Text("Action 1: SetCustomValue")
                }
                Button(onClick = {
                    viewModelActionDispatcher.dispatch(
                        ${NAME}Actions.RemoveValue
                    )
                }) {
                    Text("Action 2: RemoveValue")
                }
            }
        }
    }
}