package com.kortisan.authentication.presentation.blocs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.kortisan.authentication.domain.models.AskForPasswordModel
import com.kortisan.framework.bloc.BaseBloc
import com.kortisan.framework.bloc.BlocState
import com.kortisan.framework.bloc.ViewModelActionDispatcher

class AskForPasswordBloc (
    override val viewModelActionDispatcher: ViewModelActionDispatcher,
    override val state: BlocState<AskForPasswordModel>
): BaseBloc<AskForPasswordModel, ViewModelActionDispatcher>(
    viewModelActionDispatcher, state
) {
    @Composable
    override fun Render() {
        Text("AskForPasswordBloc")
    }
}