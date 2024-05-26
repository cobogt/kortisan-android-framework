package com.kortisan.framework.redux.stores
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
 * * * * * * * * * * **/

import com.kortisan.framework.redux.actions.ReduxAction
import com.kortisan.framework.redux.state.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Contenedor de estados para la aplicación
 */
object ApplicationStateStore {
    private val connectivityStateM =
        MutableStateFlow<ConnectivityState>( ConnectivityState.NoInternet )
    val connectivityState: StateFlow<ConnectivityState> =
        connectivityStateM

    private val sessionStateM =
        MutableStateFlow<SessionState>( SessionState.Loading )
    val sessionState: StateFlow<SessionState> =
        sessionStateM

    private val geopositionStateM =
        MutableStateFlow<GeopositionState>( GeopositionState.NoPermission )
    val geopositionState: StateFlow<GeopositionState> =
        geopositionStateM

    private val permissionStateM =
        MutableStateFlow<PermissionState>( PermissionState.NoPermission )
    val permissionState: StateFlow<PermissionState> =
        permissionStateM

    private val performanceScoreStateM =
        MutableStateFlow<PerformanceScoreState>( PerformanceScoreState.NotCalculated )
    val performanceScoreState: StateFlow<PerformanceScoreState> =
        performanceScoreStateM

    private val updateStateM =
        MutableStateFlow<UpdateState>( UpdateState.NoUpdate )
    val updateState: StateFlow<UpdateState> =
        updateStateM

    private val routeFlowNamesAliasM =
        MutableStateFlow<RouteFlowNamesAliasState>( RouteFlowNamesAliasState.Loading )
    val routeFlowNamesAlias: StateFlow<RouteFlowNamesAliasState> =
        routeFlowNamesAliasM

    private val deviceStateM =
        MutableStateFlow<DeviceState>( DeviceState.Loading )
    val deviceState: StateFlow<DeviceState> =
        deviceStateM

    private val remoteConfigStateM =
        MutableStateFlow<RemoteConfigState<Map<String, Any?>>>( RemoteConfigState.Loading() )
    val remoteConfigState: StateFlow<RemoteConfigState<Map<String, Any?>>> =
        remoteConfigStateM

    private val navigationStateM =
        MutableStateFlow<NavigationState>( NavigationState.LoadingTBD )
    val navigationState: StateFlow<NavigationState> = navigationStateM

    private val sensorsStateM =
        MutableStateFlow<SensorsState>( SensorsState.Loading )
    val sensorsState: StateFlow<SensorsState> = sensorsStateM

    // Reduce el estado de toda la aplicación a partir de reglas de producción independientes
    fun reduceAction( action: ReduxAction ) {
        updateStateM.value =            updateStateM.value.reduce( action )
        deviceStateM.value =            deviceStateM.value.reduce( action )
        sessionStateM.value =           sessionStateM.value.reduce( action )
        sensorsStateM.value =           sensorsStateM.value.reduce( action )
        navigationStateM.value =        navigationStateM.value.reduce( action )
        permissionStateM.value =        permissionStateM.value.reduce( action )
        geopositionStateM.value =       geopositionStateM.value.reduce( action )
        remoteConfigStateM.value =      remoteConfigStateM.value.reduce( action )
        connectivityStateM.value =      connectivityStateM.value.reduce( action )
        routeFlowNamesAliasM.value =    routeFlowNamesAliasM.value.reduce( action )
        performanceScoreStateM.value =  performanceScoreStateM.value.reduce( action )
    }
}
