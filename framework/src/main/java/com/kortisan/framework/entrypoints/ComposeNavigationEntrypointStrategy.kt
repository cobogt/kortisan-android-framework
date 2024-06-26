package com.kortisan.framework.entrypoints
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.ReduxAction

data class ComposeNavigationEntrypointStrategy(val exampleAttributeFlowNameFromPayload: String): EntrypointStrategy() {
    override fun getAction(): ReduxAction = ReduxAction.EmptyAction
}