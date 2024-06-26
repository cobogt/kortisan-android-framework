package com.kortisan.framework.entrypoints
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.content.Intent
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.kortisan.framework.redux.actions.ReduxAction
import java.lang.Exception

/**
 * El DeepLink es nativo de Android
 * https://developer.android.com/training/app-links/deep-linking?hl=es-419
 */
data class DeeplinkEntrypointStrategy( private val intent: Intent ): EntrypointStrategy() {
    override fun getAction(): ReduxAction =
        try {
            val deepLinkPayload = intent.getStringExtra("deeplinkPayload") ?: "{}"
            val json = JsonParser.parseString(deepLinkPayload).asJsonObject
            val flowName: String = json["flowName"]?.asString ?: ""

            getNavigationActionFromJson( json, flowName )
        } catch (e: JsonSyntaxException) {
            ReduxAction.EmptyAction
        } catch (e: JsonParseException) {
            ReduxAction.EmptyAction
        } catch (e: Exception) {
            ReduxAction.EmptyAction
        }
}