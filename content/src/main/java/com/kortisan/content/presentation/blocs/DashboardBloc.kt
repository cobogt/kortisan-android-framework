package com.kortisan.content.presentation.blocs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kortisan.content.R
import com.kortisan.content.domain.models.DashboardModel
import com.kortisan.content.presentation.states.NotificationTextState
import com.kortisan.framework.bloc.BaseBloc
import com.kortisan.framework.bloc.BlocState
import com.kortisan.framework.bloc.ViewModelActionDispatcher
import com.kortisan.framework.redux.actions.ReduxAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DashboardBloc(
    override val viewModelActionDispatcher: ViewModelActionDispatcher,
    override val state: BlocState<DashboardModel>
): BaseBloc<DashboardModel, ViewModelActionDispatcher>(
    viewModelActionDispatcher, state
) {
    private val textMessageState = MutableStateFlow<NotificationTextState>(
        NotificationTextState.TextToShow("N/D")
    )

    init { observeActions() }

    override fun reduce(action: ReduxAction) {
        textMessageState.value = textMessageState.value.reduce( action )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Render() {
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("CONTADOR DE NOTAS: 999") },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Outlined.Menu, contentDescription = "")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            bottomBar = {
                BottomAppBar(
                    actions = {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(imageVector = Icons.Outlined.Info, contentDescription = "")
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(imageVector = Icons.Outlined.Info, contentDescription = "")
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(imageVector = Icons.Outlined.Info, contentDescription = "")
                            }
                        }

                    }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text(stringResource(id = R.string.create_note)) },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                    onClick = {
                        scope.launch {
                            val result = snackbarHostState
                                .showSnackbar(
                                    message = "Snackbar",
                                    actionLabel = "Action",
                                    // Defaults to SnackbarDuration.Short
                                    duration = SnackbarDuration.Short
                                )
                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    /* Handle snackbar action performed */
                                }
                                SnackbarResult.Dismissed -> {
                                    /* Handle snackbar dismissed */
                                }
                            }
                        }
                    }
                )
            },
        ) { innerPadding ->
            LazyColumn( Modifier.padding( innerPadding ) ) {
                items( arrayOf<String>("Nota 1", "nota 2") ) { noteItem ->
                    Text( text = noteItem )
                }
            }
        }
    }
}