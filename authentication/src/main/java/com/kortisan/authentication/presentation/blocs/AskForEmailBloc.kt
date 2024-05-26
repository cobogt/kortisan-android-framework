package com.kortisan.authentication.presentation.blocs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kortisan.authentication.domain.models.AskForEmailModel
import com.kortisan.authentication.presentation.actions.SetTextAction
import com.kortisan.authentication.presentation.states.AskForEmailState
import com.kortisan.framework.bloc.BaseBloc
import com.kortisan.framework.bloc.BlocState
import com.kortisan.framework.bloc.ViewModelActionDispatcher
import com.kortisan.framework.redux.actions.ExitGateAction
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.gates.ExitGateResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

class AskForEmailBloc(
override val viewModelActionDispatcher: ViewModelActionDispatcher,
override val state: BlocState<AskForEmailModel>
): BaseBloc<AskForEmailModel, ViewModelActionDispatcher>(
viewModelActionDispatcher, state
) {
    private val internalState = MutableStateFlow<AskForEmailState>(
        runBlocking {
            state.componentState.map {
                AskForEmailState.EmptyInput.reduce<AskForEmailState>(
                    SetTextAction( it.email )
                )
            }.firstOrNull() ?: AskForEmailState.EmptyInput
        }
    )

    init { observeActions() }

    override fun reduce(action: ReduxAction) {
        internalState.value = internalState.value.reduce( action )
    }

    @Composable
    override fun Render() {
        val state = internalState.collectAsState()

        var text by remember { mutableStateOf("") }
        var showErrorMessage by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }

        showErrorMessage = false

        when( val currentState = state.value ) {
            AskForEmailState.EmptyInput ->
                text = ""

            is AskForEmailState.ErrorOnInput -> {
                // text = currentState.currentValue

                errorMessage = currentState.errorMessage

                showErrorMessage = true
            }

            is AskForEmailState.SuccessInput -> {
                //text = currentState.currentValue
            }
        }

        Column() {
            Text(text = "AskForEmailBloc")

            Spacer(modifier = Modifier.height(10.dp))
            if( showErrorMessage )
                Text("Error: $errorMessage")

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = text, onValueChange = {
                viewModelActionDispatcher.dispatch(
                    SetTextAction( it )
                )
                text = it
            },
                label = { Text("Email") },
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                 viewModelActionDispatcher.dispatch(
                     ExitGateAction(
                         ExitGateResult.Success(null, null)
                     )
                 )
             }, enabled = ! showErrorMessage && text.isNotEmpty() ) {
                Text("Continuar")
            }
        }
    }
}