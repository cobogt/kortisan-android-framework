package com.kortisan.framework.redux.controllers.navigation.targets
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.controllers.navigation.PrimitiveTargetActivityIdentifier
import com.kortisan.framework.redux.controllers.navigation.protocol.NavigationTarget

sealed class TargetActivity(
    val path: String,
    val primitiveTargetActivityIdentifier: PrimitiveTargetActivityIdentifier,
): NavigationTarget()
