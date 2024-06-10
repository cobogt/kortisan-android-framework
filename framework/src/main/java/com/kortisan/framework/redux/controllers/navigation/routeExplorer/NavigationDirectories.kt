package com.kortisan.framework.redux.controllers.navigation.routeExplorer

import android.util.Log
import com.kortisan.framework.redux.controllers.navigation.PrimitiveTargetActivityIdentifier
import com.kortisan.framework.redux.controllers.navigation.protocol.NavigationTarget
import com.kortisan.framework.redux.controllers.navigation.targets.TargetActivity

/**
 * Contiene todas las rutas navegables por la aplicación
 * TODO: Construir esta clase en tiempo de compilación
 */
data object NavigationDirectories {
    val TAG: String = javaClass.simpleName

    // DIRECTORIO 1: Almacenamos todas las rutas usando el Path como llave, los parámetros que
    // puede recibir usando el nombre como llave y el valor corresponde al tipo
    val routerParamMap: MutableMap<String, Map<String, String>> =
        mutableMapOf()

    // DIRECTORIO 2: Almacenamos todos los objetivos de navegación usando el Path como llave
    val routerMap: MutableMap<String, NavigationTarget> =
        mutableMapOf()

    // DIRECTORIO 3: Almacenamos todas las rutas a las actividades usando el FlowName como
    // llave <flowName, ActivityClassName>
    val flowNameMap: MutableMap<String, String> =
        mutableMapOf()

    // DIRECTORIO 4: Almacenamos todos los objetivos de navegación de actividad usando el nombre
    // de la clase Activity como llave
    val activityMap: MutableMap<String, TargetActivity> =
        mutableMapOf()

    // DIRECTORIO 5: Almacenamos la relación entre el flowName y el nombre de
    // la clase de la actividad
    val flowNameActivityMap: MutableMap<String, PrimitiveTargetActivityIdentifier> =
        mutableMapOf()

    // DIRECTORIO 6: Almacenamos la relación entre un objetivo de navegación y las
    // diferentes configuraciones de los parámetros que recibe
    val paramsTargetsMap: MutableMap<NavigationTarget, MutableList<Map<String, String>>> =
        mutableMapOf()

    // DIRECTORIO 7: Almacenamos la relación entre el FlowName y el objetivo de navegación
    val flowNameTargetMap: MutableMap<String, NavigationTarget> =
        mutableMapOf()

    // DIRECTORIO 8: Almacenamos la relación entre los FlowName y sus parámetros
    // predeterminados para instanciarlo
    val flowNameDefaultParamsMap: MutableMap<String, String> =
        mutableMapOf()

    // DIRECTORIO 9: Alias para los flowname <Alias, FlowNameOriginal>
    val flowNameAliasMap: MutableMap<String, String> =
        mutableMapOf()

    fun logDirectories() {
        StringBuilder()
            .appendLine()
            .toString()
        Log.d(RouteExplorer.TAG, "== (${routerParamMap.count()}) ==")
        Log.d(
            RouteExplorer.TAG, "== DIRECTORIO 1: Almacenamos todas las rutas usando el Path" +
                " como llave, los parámetros que puede recibir usando el nombre como llave" +
                " y el valor corresponde al tipo ========")
        routerParamMap.entries.forEach {
            Log.d(RouteExplorer.TAG, "${it.key} => ${it.value}")
        }

        Log.d(RouteExplorer.TAG, "== (${routerMap.count()}) ==")
        Log.d(
            RouteExplorer.TAG, "== DIRECTORIO 2: Almacenamos todos los objetivos de navegación" +
                " usando el Path como llave ========")
        routerMap.entries.forEach {
            Log.d(RouteExplorer.TAG, "${it.key} => ${it.value}")
        }

        Log.d(RouteExplorer.TAG, "== (${flowNameMap.count()}) ==")
        Log.d(
            RouteExplorer.TAG, "== DIRECTORIO 3: Almacenamos todas las rutas a las actividades" +
                " usando el FlowName como llave <flowName, ActivityClassName> ========")
        flowNameMap.entries.forEach {
            Log.d(RouteExplorer.TAG, "${it.key} => ${it.value}")
        }

        Log.d(RouteExplorer.TAG, "== (${activityMap.count()}) ==")
        Log.d(
            RouteExplorer.TAG, "== DIRECTORIO 4: Almacenamos todos los objetivos de navegación" +
                " de actividad usando el nombre de la clase Activity como llave ========")
        activityMap.entries.forEach {
            Log.d(RouteExplorer.TAG, "${it.key} => ${it.value}")
        }

        Log.d(RouteExplorer.TAG, "== (${flowNameActivityMap.count()}) ==")
        Log.d(
            RouteExplorer.TAG, "== DIRECTORIO 5: Almacenamos la relación entre el flowName y el" +
                " nombre de la clase de la actividad ========")
        flowNameActivityMap.entries.forEach {
            Log.d(RouteExplorer.TAG, "${it.key} => ${it.value}")
        }

        Log.d(RouteExplorer.TAG, "== (${paramsTargetsMap.count()}) ==")
        Log.d(
            RouteExplorer.TAG, "== DIRECTORIO 6: Almacenamos la relación entre un objetivo de" +
                " navegación y los parámetros que recibe ========")
        paramsTargetsMap.entries.forEach {
            Log.d(RouteExplorer.TAG, "${it.key} => ${it.value}")
        }

        Log.d(RouteExplorer.TAG, "== (${flowNameTargetMap.count()}) ==")
        Log.d(
            RouteExplorer.TAG, "== DIRECTORIO 7: Almacenamos la relación entre el FlowName y el" +
                " objetivo de navegación ========")
        flowNameTargetMap.entries.forEach {
            Log.d(RouteExplorer.TAG, "${it.key} => ${it.value}")
        }

        Log.d(RouteExplorer.TAG, "== (${flowNameDefaultParamsMap.count()}) ==")
        Log.d(
            RouteExplorer.TAG, "== DIRECTORIO 8: Almacenamos la relación entre los FlowName y sus" +
                " parámetros predeterminados para instanciarlo ========")
        flowNameDefaultParamsMap.entries.forEach {
            Log.d(RouteExplorer.TAG, "${it.key} => ${it.value}")
        }

        Log.d(RouteExplorer.TAG, "== (${flowNameAliasMap.count()}) ==")
        Log.d(
            RouteExplorer.TAG, "== DIRECTORIO 9: Almacenamos la relación entre el " +
                "alias y el FlowName ========")
        flowNameAliasMap.entries.forEach {
            Log.d(RouteExplorer.TAG, "${it.key} => ${it.value}")
        }
        Log.d(RouteExplorer.TAG, "=============================")
    }
}