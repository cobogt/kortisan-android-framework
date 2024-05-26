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

/***
 * OneLink es controlado por AppsFlyer
 * https://support.appsflyer.com/hc/es/articles/115005248543-Descripci%C3%B3n-general-de-OneLink
 */
data class OneLinkEntrypointStrategy( private val intent: Intent): EntrypointStrategy() {
    override fun getAction(): ReduxAction =
        try {
            val oneLinkPayload = intent.getStringExtra("onelinkPayload") ?: "{}"
            val json = JsonParser.parseString( oneLinkPayload ).asJsonObject
            val flowName = json["targetFlowName"]?.asString ?: ""

            getNavigationActionFromJson( json, flowName, true )
        }catch (e: JsonSyntaxException) {
            ReduxAction.EmptyAction
        }catch (e: JsonParseException) {
            ReduxAction.EmptyAction
        }catch (e: java.lang.Exception) {
            ReduxAction.EmptyAction
        }
    }

