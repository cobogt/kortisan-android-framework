package com.kortisan.framework.entrypoints
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.google.gson.JsonObject
import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.controllers.navigation.NavigationController
import com.kortisan.framework.redux.controllers.tagging.builders.EntryPointSceneBuilder
import okhttp3.internal.toImmutableMap

/**
 * Esta clase tiene las estrategias necesarias para iniciar la navegación dentro de la app
 * independiente del punto de entrada
 */
sealed class EntrypointStrategy {
    abstract fun getAction(): ReduxAction

    companion object {
        internal val taggingScene = EntryPointSceneBuilder
            .Builder("application")
            .setFlowName( "default" )

        internal fun getNavigationActionFromMap(
            jsonMap:      Map<String, String>,
            flowName:     String,
            addAllParams: Boolean = false
        ): ReduxAction =
            if( flowName.isNotEmpty() ) {
                // Obtenemos el objetivo de navegación desde el flowName
                val navigationTarget = NavigationController().getNavigationTargetFromFlowName(
                    flowName
                )

                val navigationTargetParams = mutableMapOf<String, String>()

                val requiredParams = NavigationController().getNavigationParamsForTarget(
                    navigationTarget
                )

                // Identificamos los parámetros requeridos y los buscamos en el JSON
                // Seleccionamos la primer configuración cuyo json satisfaga todos los parámetros
                var paramConfig = requiredParams.filter { it.isNotEmpty() }.firstOrNull { map ->
                    var containsAll = true
                    map.values.forEach { containsAll = containsAll.and( jsonMap.containsKey( it )) }
                    containsAll
                }?.onEach {
                    navigationTargetParams[ it.key ] = jsonMap[it.key] ?: ""
                }

                // Si no se encontró una solución que cumpla con los parámetros, se busca una
                // alternativa sin parámetros
                if( paramConfig == null && requiredParams.filter { it.isEmpty() }.isNotEmpty() )
                    paramConfig = mapOf()

                if( addAllParams )
                    jsonMap.forEach { (key, value) ->
                        if( ! navigationTargetParams.containsKey( key ))
                            navigationTargetParams[ key ] = value
                    }

                // Si el objeto tiene todos los parámetros necesarios construimos la navegación
                //if( navigationTargetParams.size == requiredParams.size )
                if( paramConfig != null )
                    NavigationActions.NavigateToTargetWithParams(
                        navigationTarget,
                        navigationTargetParams
                    ).apply {
                        val origin: String = Thread.currentThread()
                            .stackTrace
                            .getOrNull( 2 )
                            ?.className ?: "unknown"

                        tagging = taggingScene
                            .setEvent( "launch" )
                            .setOrigin( origin )
                            .build()
                    }
                else
                    ReduxAction.EmptyAction
            } else
                ReduxAction.EmptyAction

        internal fun getNavigationActionFromJson(
            json:         JsonObject,
            flowName:     String,
            addAllParams: Boolean = false
        ): ReduxAction = mutableMapOf<String, String>().apply {
            json.keySet().forEach {
                this[ it ] = json[ it ]?.asString ?: ""
            }
        }.let {
            getNavigationActionFromMap( it.toImmutableMap(), flowName, addAllParams )
        }
    }
}
