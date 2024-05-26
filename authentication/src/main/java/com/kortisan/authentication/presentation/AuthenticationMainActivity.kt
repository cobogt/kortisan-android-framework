package com.kortisan.authentication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kortisan.authentication.domain.AuthenticationViewModel
import com.kortisan.framework.bloc.ReduxViewModelFactory
import com.kortisan.framework.bloc.SingleActivityWithViewModel
import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.navigation.targets.AuthenticationNavigationGroup
import com.kortisan.framework.redux.controllers.navigation.targets.TargetCompose
import kotlinx.coroutines.launch

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

class AuthenticationMainActivity: ComponentActivity(), SingleActivityWithViewModel {
    override val singleActivityViewModel: AuthenticationViewModel by viewModels {
        ReduxViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()

            navController.enableOnBackPressed( true )

            NavHost(navController = navController, startDestination = "/") {
                composable("/") {
                    Text(text = "Raiz")
                }

                composable("/login") {
                    singleActivityViewModel.loginBloc.Render()
                }

                composable("/read_biometrics") {
                    singleActivityViewModel.readBiometricsBloc.Render()
                }

                composable("/ask_for_email") {
                    singleActivityViewModel.askForEmailBloc.Render()
                }
            }

            LaunchedEffect( lifecycleScope ) {
                coroutineScope.launch {
                    singleActivityViewModel.getDispatcher().currentAction.collect { currentAction ->

                        if(currentAction is NavigationActions.NavigateToTarget &&
                            currentAction.target is TargetCompose<*>
                            && (currentAction.target as TargetCompose<*>)
                                .targetActivity == AuthenticationNavigationGroup.Authentication
                        ) {
                            navController.navigate(
                                (currentAction.target as TargetCompose<*>).path
                            ) {
                                popUpTo( navController.graph.findStartDestination().id ) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }

                            singleActivityViewModel
                                .getDispatcher()
                                .dispatch( ReduxAction.EmptyAction )
                        }
                    }
                }
            }
        }
    }
}