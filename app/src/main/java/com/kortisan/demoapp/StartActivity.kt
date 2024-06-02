package com.kortisan.demoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kortisan.framework.entrypoints.IntentEntrypointStrategy
import com.kortisan.framework.redux.ApplicationActionDispatcher
import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.ControllersProxy
import com.kortisan.framework.redux.controllers.navigation.NavigationController
import com.kortisan.framework.redux.gates.AskForEmailGate
import com.kortisan.framework.redux.gates.FingerprintGate
import com.kortisan.framework.redux.gates.LoggedGate

class StartActivity: AppCompatActivity() {
    private val navigationController = ControllersProxy.getController<NavigationController>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var startAction = IntentEntrypointStrategy( intent ).getAction()

        if( startAction is ReduxAction.EmptyAction )
            navigationController?.defaultNavigationTarget?.also {
                startAction = NavigationActions.NavigateToTarget( it )
            }

        finishAffinity()

        // Despachamos una acción al bus de eventos sin que repercuta en una vista
        ApplicationActionDispatcher.dispatch ( startAction.apply {
            // Aplicamos la compuerta que comprueba el estado de login del usuario
                 gates = listOf( LoggedGate )
            }
        )
    }
}