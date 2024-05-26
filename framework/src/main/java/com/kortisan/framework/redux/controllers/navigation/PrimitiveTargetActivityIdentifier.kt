package com.kortisan.framework.redux.controllers.navigation
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

data class PrimitiveTargetActivityIdentifier(
    val className:     String,
    val flowName:      String = "",
    val section:       String = "",
    val flowNameAlias: FlowNameAlias? = null
)
