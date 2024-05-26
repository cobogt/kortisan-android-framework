package com.kortisan.framework.redux.controllers.navigation.targets
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.net.Uri
import com.kortisan.framework.redux.controllers.navigation.protocol.NavigationTarget

sealed class TargetExternal (
    val action:  String,
    val uri:     Uri?,
    val path:    String? = null,
    val flowName: String? = null,
): NavigationTarget()
