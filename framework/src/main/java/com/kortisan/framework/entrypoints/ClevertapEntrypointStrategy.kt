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

class ClevertapEntrypointStrategy( private val intent: Intent ): EntrypointStrategy() {
    override fun getAction(): ReduxAction =
        try {
            val clevertapPayload = intent.getStringExtra("clevertapPayload") ?: "{}"
            val json = JsonParser.parseString( clevertapPayload ).asJsonObject
            val flowName = json["targetFlowName"]?.asString ?: ""

            getNavigationActionFromJson( json, flowName)
        }catch (e: JsonSyntaxException) {
            ReduxAction.EmptyAction
        }catch (e: JsonParseException) {
            ReduxAction.EmptyAction
        }catch (e: Exception) {
            ReduxAction.EmptyAction
        }
}