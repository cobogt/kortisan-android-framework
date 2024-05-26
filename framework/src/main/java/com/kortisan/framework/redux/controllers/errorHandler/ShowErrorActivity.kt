package com.kortisan.framework.redux.controllers.errorHandler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kortisan.framework.redux.ApplicationActionDispatcher
import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.controllers.navigation.NavigationController
import com.kortisan.framework.toMap
import com.kortisan.ksp.annotations.FlowNameActivity

@FlowNameActivity("errorHandlerFlowName")
class ShowErrorActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val params: Map<String, String> = intent.extras?.toMap() ?: emptyMap()

        setContent {
            Column {
                Text(text = "Manejador de errores")
                Text(text = "Titulo: ${params["title"]}")
                Text(text = "Descripción: ${params["description"]}")
                Text(text = "Nombre de la excepción: ${params["exceptionName"]}")
                Text(text = "Mensaje de la excepción: ${params["exceptionMessage"]}")

                Spacer(modifier = Modifier.height(25.dp))
                Button(onClick = {
                    finishAffinity()
                    // TODO: Mejorar este código
                    ApplicationActionDispatcher.dispatch(
                        NavigationActions.NavigateToTarget(
                            NavigationController().defaultNavigationTarget
                        )
                    )
                }) {
                    Text(text = "Continuar")
                }
            }
        }
    }
}