package com.kortisan.framework.redux.state

sealed class RouteFlowNamesAliasState: ReduxState() {
    data class AliasMap(
        val flowNameAliasMap: Map<String, String> = mapOf()
    ): RouteFlowNamesAliasState()
    object Loading: RouteFlowNamesAliasState()
}