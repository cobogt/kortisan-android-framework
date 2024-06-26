package com.kortisan.framework.redux.state
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.controllers.remoteConfig.parsers.RemoteConfigDirectoryMap
import com.kortisan.framework.redux.state.productionrules.ProductionRule
import java.lang.Exception
import kotlin.reflect.full.cast

sealed class RemoteConfigState<T>: ReduxState() {
    companion object {
        val objectMap: MutableMap<String, Any?> = mutableMapOf()

        inline fun <reified T: Any?> getContent(key: String): T? =
            try {
                RemoteConfigDirectoryMap.parserMap[ key ]?.cast(objectMap[ key ]) as T
            } catch (e: Exception) {
                null // Error al castear
            }
    }

    data class SuccessLoad<T> (
        val config: T
        ): RemoteConfigState<T>() {
        override val productionRules: List<ProductionRule> = sharedProductionRules
    }

    data class CacheLoad<T> (
        val configCache: T
    ): RemoteConfigState<T>() {
        override val productionRules: List<ProductionRule> = sharedProductionRules
    }

    data class Error<T> (
        val message: String,
        val exception: Exception? = null
    ): RemoteConfigState<T>() {
        override val productionRules: List<ProductionRule> = sharedProductionRules
    }

    data class Loading<T> (
        val any: T? = null
    ): RemoteConfigState<T>() {
        override val productionRules: List<ProductionRule> = sharedProductionRules
    }

    protected val sharedProductionRules: List<ProductionRule> = listOf()
}
/*                if (
//                    _remoteConfigState.value is RemoteConfigState.SuccessLoad ||
//                    _remoteConfigState.value is RemoteConfigState.CacheLoad
                ) {
//                    _remoteConfigState.value = RemoteConfigState.CacheLoad(_objectMap)
                } else {
//                    _remoteConfigState.value = RemoteConfigState.Error(
//                        "Error al obtener la información" +
//                                ", se convierte en caché: ${it.localizedMessage}",
//                        it
//                    )
                }*/