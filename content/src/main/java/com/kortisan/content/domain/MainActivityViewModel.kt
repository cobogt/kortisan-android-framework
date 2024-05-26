package com.kortisan.content.domain

import android.app.Application
import com.kortisan.content.domain.models.DashboardModel
import com.kortisan.content.presentation.blocs.DashboardBloc
import com.kortisan.content.presentation.tagging.MainActivitySceneBuilder
import com.kortisan.framework.bloc.BlocState
import com.kortisan.framework.bloc.ReduxViewModelBase
import com.kortisan.framework.bloc.ViewModelActionDispatcher
import com.kortisan.framework.redux.stores.ApplicationStateStore
import kotlinx.coroutines.flow.flow

class MainActivityViewModel(
    application: Application,
    vmActionDispatcher: ViewModelActionDispatcher
): ReduxViewModelBase( application, vmActionDispatcher )  {
    private val mainActivitySceneBase = MainActivitySceneBuilder.Builder()
        .setModuleName("Main")
        .build()

    val dashboardRootBLoC = DashboardBloc(
        getDispatcher(),
        BlocState(
            ApplicationStateStore,
            mainActivitySceneBase,
            flow { emit(DashboardModel("FLOW VALUE")) }
        )
    )
}