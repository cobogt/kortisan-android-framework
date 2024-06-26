package com.kortisan.framework.entrypoints
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.redux.actions.ReduxAction
import java.lang.Exception

data class PushNotificationEntrypointStrategy(
    val data: Map<String, String>
): EntrypointStrategy() {
    override fun getAction(): ReduxAction = try {
        val flowName = data["targetFlowName"]
            ?: ""

        if( flowName.isNotEmpty() )
            getNavigationActionFromMap( data, flowName )
        else
            data["action"]
                ?.let { actionName ->
                    // Acción de redux desde el catálogo
                    ReduxAction.getAction( actionName, data )
                }?: ReduxAction.EmptyAction
    } catch (e: Exception) {
        ReduxAction.EmptyAction
    }
}