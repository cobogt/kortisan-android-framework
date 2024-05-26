package com.kortisan.framework.redux.controllers.remoteConfig.parsers
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import kotlin.reflect.KClass

object RemoteConfigDirectoryMap {
    val parserMap: Map<String, KClass<*>> = mapOf(
        "flowNameAlias"    to FlowNameAliasRemoteConfig::class,
        "taggingSettings" to TaggingSettingsRemoteConfig::class,
    )

    var defaultAsyncMap: Map<String, Any> = mapOf(
        "byte"    to ByteArray(0),
        "Boolean" to false,
        "Double"  to "0".toDouble(),
        "Long"    to 0L,
        "String"  to "{}"
    )
}
