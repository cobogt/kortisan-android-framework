package com.kortisan.framework.redux.controllers.remoteConfig.parsers
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

data class FlowNameAliasRemoteConfig(
    val available: List<FlowAlias> = listOf(),
    val blocked:   List<String>    = listOf()
) {
    data class FlowAlias(
        val flowName: String = "",
        val alias:   String = "",
    )
}
