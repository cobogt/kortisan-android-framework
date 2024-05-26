package com.kortisan.framework.entrypoints
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.content.Intent
import android.net.Uri
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.kortisan.framework.redux.controllers.navigation.targets.TargetActivity
import com.kortisan.framework.redux.controllers.navigation.targets.TargetCompose
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.navigation.NavigationController
import java.lang.Exception

/**
 * AppLinks para navegación en la aplicación con rutas web externas
 * https://developer.android.com/studio/write/app-link-indexing?hl=es-419
 */
data class AppLinksEntrypointStrategy(val intent: Intent): EntrypointStrategy() {
    override fun getAction(): ReduxAction =
        try {
            val params = mutableMapOf<String, String>()
            var flowName = ""

            val appLinkAction = intent.action
            val appLinkData: Uri? = intent.data

            if (Intent.ACTION_VIEW == appLinkAction) {
                appLinkData?.also { uri ->
                    // Solamente abrimos los enlaces de la app
                    if( uri.host == "demoapp.app" ) {
                        // Provisionalmente no aceptamos parámetros
                        val navigationTarget = NavigationController().getNavigationTargetForRoute(
                            uri.path?.trim('/') ?: "/"
                        )

                        val paramsValuesInUri = uri.path
                            ?.split(
                                Regex("\\{[a-zA-Z\\d_\\-]+}")
                            )?.map {
                                it.trim('{', '}')
                            }?: listOf()

                        when( navigationTarget ) {
                            is TargetActivity -> {
                                flowName = navigationTarget
                                    .primitiveTargetActivityIdentifier
                                    .flowName

                                // Agregamos todos los parámetros del query
                                uri.queryParameterNames.forEach {
                                    params[ it ] = uri.getQueryParameter( it ) ?: ""
                                }

                                val paramsNamesInUri = navigationTarget
                                    .path
                                    .split(
                                        Regex("\\{[a-zA-Z\\d_\\-]+}")
                                    ).map {
                                        it.trim('{', '}')
                                    }

                                paramsNamesInUri.forEachIndexed { index, paramName ->
                                    paramsValuesInUri.getOrNull( index )?.also { paramValue ->
                                        params[ paramName ] = paramValue
                                    }
                                }
                            }
                            is TargetCompose<*> -> {
                                /*
                                 * Si el objetivo es composable, se debe inyectar la acción a la
                                 * vista para que continue con ella
                                 */
                            }
                        }
                    }
                }
            }

            getNavigationActionFromMap( params, flowName, true )
        }catch (e: JsonSyntaxException) {
            ReduxAction.EmptyAction
        }catch (e: JsonParseException) {
            ReduxAction.EmptyAction
        }catch (e: Exception) {
            ReduxAction.EmptyAction
        }
}
