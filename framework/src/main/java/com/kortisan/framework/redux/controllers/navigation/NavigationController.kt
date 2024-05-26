package com.kortisan.framework.redux.controllers.navigation
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.app.Activity
import android.util.Log
import androidx.navigation.ActivityNavigator
import androidx.navigation.ActivityNavigatorDestinationBuilder
import com.kortisan.framework.FrameworkApplicationBinding
import com.kortisan.framework.redux.actions.NavigationActions
import com.kortisan.framework.redux.controllers.ReduxControllerAbstract
import com.kortisan.framework.redux.controllers.ReduxControllerState
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.activityMap
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.flowNameAliasMap
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.flowNameDefaultParamsMap
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.flowNameTargetMap
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.paramsTargetsMap
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.NavigationDirectories.routerMap
import com.kortisan.framework.redux.controllers.navigation.protocol.NavigationTarget
import com.kortisan.framework.redux.controllers.navigation.routeExplorer.RouteExplorer
import com.kortisan.framework.redux.controllers.navigation.strategies.NavigationStrategy
import com.kortisan.framework.redux.controllers.navigation.targets.*
import com.kortisan.framework.redux.stores.ApplicationStateStore
import com.kortisan.framework.redux.state.RemoteConfigState
import com.kortisan.framework.redux.controllers.remoteConfig.parsers.FlowNameAliasRemoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.full.starProjectedType

class NavigationController: ReduxControllerAbstract() {
    val TAG: String = javaClass.simpleName

    // Destino predeterminado
    val defaultNavigationTarget = DashboardNavigationGroup.Dashboard

    override fun start() {
        // Inicialización de rutas de la aplicación
        RouteExplorer.detectNavigationTargets()

        // Cargamos los alias de las rutas de navegación
        loadFlowNamesAlias()

        // Marcamos el controlador como inicializado, aka. super.start()
        setStartStatus(
            ReduxControllerState.Started
        )
    }

    inline fun <reified T: TargetActivity>getTargetActivityFor( activity: Activity ) =
        activityMap.getOrDefault(
            activity.javaClass.name,
            defaultNavigationTarget
        ) as T

    fun getNavigationTargetForRoute(route: String ): NavigationTarget =
        routerMap.getOrDefault( route, defaultNavigationTarget )

    fun getNavigationParamsForClass( className: String ): NavigationTarget =
        activityMap.getOrDefault( className,
            defaultNavigationTarget
        )

    fun getClassNameForNavigationAction( action: NavigationActions ): String =
        when( action ) {
            is NavigationActions.NavigateToTarget ->
                getActivityClassNameForTarget( action.target )
            is NavigationActions.NavigateToTargetWithParams ->
                getActivityClassNameForTarget( action.target )
            is NavigationActions.NavigateToClass ->
                action.className
            is NavigationActions.NavigateToRoute -> ""
        }

    private fun getActivityClassNameForTarget(target: NavigationTarget ): String =
        when( target ) {
            is TargetActivity   -> target.primitiveTargetActivityIdentifier.className
            is TargetCompose<*> -> target.targetActivity.primitiveTargetActivityIdentifier.className
            is TargetClass      -> target.classInPackage // TODO: Comp  robar que sea un activity
            is TargetExternal   -> ""
            else                -> ""
        }

    /**
     * Observamos el estado de la aplicación para cargar
     * los alias de los flowname cuando este cambie.
     */
    private fun loadFlowNamesAlias() {
        CoroutineScope( Dispatchers.IO ).launch {
            ApplicationStateStore.remoteConfigState.collect { remoteConfigState ->
                if( remoteConfigState is RemoteConfigState.SuccessLoad )
                    RemoteConfigState.getContent<FlowNameAliasRemoteConfig>("saFlowNameAlias")
                        ?.run {
                            available.forEach {
                                RouteExplorer.setFlowNameAlias( it.flowName, alias = it.alias )
                            }
                        }
            }
        }
    }

    /**
     * Devuelve el objetivo de navegación a partir de un flowName
     * Si no existe el flowName intenta encontrar su alias.
     * En caso de no existir el alias devuelve el objetivo de navegación predeterminado
     */
    fun getNavigationTargetFromFlowName(flowName: String): NavigationTarget {
        val targetFromFlowName      = flowNameTargetMap[ flowName ]
        val aliasFlowName           = flowNameAliasMap[ flowName ] ?: ""
        val targetFromFlowNameAlias = flowNameTargetMap[ aliasFlowName ]

        return targetFromFlowName ?: targetFromFlowNameAlias ?: defaultNavigationTarget
    }

    /**
     * Devuelve la lista de nombres de los parámetros que puede recibir ese objetivo de navegación
     * Esta función ayuda a crear la lista de parámetros que recibirá la estrategia de entrada
     */
    fun getNavigationParamsForTarget(target: NavigationTarget): List< Map<String, String> > =
        paramsTargetsMap.getOrDefault( target, listOf( mapOf() ) )

    /**
     * Navegamos a un objetivo que es una instancia que salió de un data class tipo TargetActivity
     * se deben extraer los parámetros de la instancia
     */
    fun navigateToTarget( target: NavigationTarget ) {
        val params = getParamsFromClassInstance( target )

        when ( target ) {
            is TargetActivity ->
                navigateToTargetWithParams( target, params )
            is TargetExternal ->
                NavigationStrategy.navigateToExternal( target.action, target.uri )
            is TargetClass ->
                NavigationStrategy.navigateToInternalClass( target, params )
            is TargetCompose<*> -> {
                // Considerar obtener los parámetros del path ???
                navigateToTargetWithParams( target, params )
            }
        }
    }

    private fun getParamsFromClassInstance(instance: Any?): Map<String, String> {
        val params = mutableMapOf<String, String>()

        try {
            instance?.javaClass?.run {
                declaredFields.forEach { field ->
                    when( field.genericType ) {
                        String::class.starProjectedType  -> params[ field.name ] =
                            field.get( instance ) as String? ?: ""

                        Int::class.starProjectedType     -> params[ field.name ] =
                            (field.get( instance ) as Int? ?: 0).toString()

                        Boolean::class.starProjectedType -> params[ field.name ] =
                            (field.get( instance ) as Boolean? ?: false).toString()

                        Float::class.starProjectedType   -> params[ field.name ] =
                            (field.get( instance ) as Float? ?: 0F).toString()

                        Long::class.starProjectedType    -> params[ field.name ] =
                            (field.get( instance ) as Long? ?: 0L).toString()
                    }
                }
            }
        }catch (e: Exception) {
            Log.e(TAG, "navigateToTarget: Error al extraer los parámetros de la instancia")
        }

        return params
    }

    /**
     * Navegamos a un objetivo que es una instancia que salió de un
     * data class tipo NavigationTargetParams, se deben extraer los parámetros de la instancia
     */
    fun navigateToTarget( target: NavigationTargetParams<*> ) {
        val ( className, params ) = RouteExplorer.getClassParamsFromNavigationTargetParams(
            target
        )

        if( className.isNotEmpty() )
            NavigationStrategy.navigateToActivityClassWithoutResult(
                className,
                params
            )
    }

    /**
     * Navega a una sección de la aplicación e inyecta lo necesario.
     * Navegar al objetivo depués de una búsqueda, si no se encuentra ir al default
     * si es la misma actividad, solo pasar los parámetros al dispatcher del viewmodel.
     */
    fun navigateToTargetWithParams(target: NavigationTarget, targetParams: Map<String, String>) {
        val params: MutableMap<String, String> = targetParams.toMutableMap()

        ActivityNavigator( FrameworkApplicationBinding.appContext ).also {
            ActivityNavigatorDestinationBuilder( it, route = "" )
        }

        val className = when( target ) {
            is TargetActivity
                -> { target.primitiveTargetActivityIdentifier.className }
            is TargetCompose<*>
                -> { target.targetActivity.primitiveTargetActivityIdentifier.className }
            else -> { "" }
        }

        val flowName = when( target ) {
            is TargetActivity
                -> target.primitiveTargetActivityIdentifier.flowName
            is TargetCompose<*>
                -> target.targetActivity.primitiveTargetActivityIdentifier.flowName
            else -> ""
        }

        try {
            flowNameDefaultParamsMap[ flowName ]?.also {
                Log.d(TAG, "KEY_PARAMS Target: $target  => $it")
                params["KEY_PARAMS"] = it // KEY_BUNDLE
            }

            NavigationStrategy.navigateToActivityClassWithoutResult(
                className,
                params
            )
        }catch ( e: Exception ) {
            Log.e(TAG, "navigateToTarget: ${e.localizedMessage}")
        }
    }

    fun navigateToRoute(route: String, params: Map<String, String>) {
        val activityPath = route.substringBefore("@")
        val composePath = route.substringAfter("@")

        Log.d(TAG, "PENDIENTE: Navegando a ruta $route con los parametros: $params " +
                "Activity path $activityPath " +
                "Compose path $composePath")
    }
}