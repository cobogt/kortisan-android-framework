package com.kortisan.authentication.presentation.blocs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.kortisan.framework.bloc.BaseBloc
import com.kortisan.framework.bloc.BlocState
import com.kortisan.framework.bloc.ViewModelActionDispatcher
import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.controllers.navigation.targets.AuthenticationNavigationGroup

class LoginBloc(
    override val viewModelActionDispatcher: ViewModelActionDispatcher,
    override val state: BlocState<String>
): BaseBloc<String, ViewModelActionDispatcher>(
    viewModelActionDispatcher, state
) {
    @Composable
    override fun Render() {
        Column() {
            Text(text = "LoginBloc")
            Button(onClick = {
                // Enviamos una acción al despachador del viewModel de la actividad actual
                viewModelActionDispatcher.dispatch(
                    NavigationActions.NavigateToTarget(
                        AuthenticationNavigationGroup.Authentication.AskForEmailCompose
                    )
                )
            }) {
                Text(text = "GOTO AskFor Email")
            }
        }
    }
}