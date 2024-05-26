package com.kortisan.framework.redux.controllers.navigation.targets
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.controllers.navigation.protocol.NavigationTarget

sealed class NavigationTargetParams<T: NavigationTarget>(val path: String = "")
// sealed class NavigationTargetParams<T: NavigationTarget>(val targetActivity: T, val path: String = "")
