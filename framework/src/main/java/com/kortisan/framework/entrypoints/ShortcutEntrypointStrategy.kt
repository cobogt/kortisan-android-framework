package com.kortisan.framework.entrypoints
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.NavigationActions

class ShortcutEntrypointStrategy(val shortcutId: String): EntrypointStrategy() {
    override fun getAction(): NavigationActions {
        TODO("Not yet implemented")
    }
}