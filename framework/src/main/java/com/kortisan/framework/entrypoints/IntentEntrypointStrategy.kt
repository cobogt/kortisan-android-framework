package com.kortisan.framework.entrypoints
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.content.Intent
import com.kortisan.framework.redux.actions.ReduxAction

data class IntentEntrypointStrategy( private val intent: Intent ): EntrypointStrategy() {
    /**
     * Lista de los entrypoints que ocupan el intent
     */
    private val dependentEntryPoints = listOf(
        AppLinksEntrypointStrategy ( intent ),
        ClevertapEntrypointStrategy( intent ),
        OneLinkEntrypointStrategy  ( intent ),
        DeeplinkEntrypointStrategy ( intent ),
    )

    override fun getAction(): ReduxAction = dependentEntryPoints
            .map { it.getAction() }
            .firstOrNull { it !is ReduxAction.EmptyAction }
        ?: ReduxAction.EmptyAction
}