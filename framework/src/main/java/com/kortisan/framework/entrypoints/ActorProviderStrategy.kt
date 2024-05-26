package com.kortisan.framework.entrypoints

import android.content.Intent
import com.kortisan.framework.redux.actions.ReduxAction

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 06/01/23.
 * * * * * * * * * * **/
data class ActorProviderStrategy( private val intent: Intent ): EntrypointStrategy() {
    override fun getAction(): ReduxAction {
        TODO("Not yet implemented")
    }
}