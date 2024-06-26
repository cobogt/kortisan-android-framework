package com.kortisan.framework
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
 * * * * * * * * * * **/

import android.os.Bundle
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.text.Normalizer

/**
 * Analizamos de forma recursiva el contenido de un mapa para buscar JSON en los Strings
 * en todos los valores. Cuando encontramos un JSON este se agrega al resultado del mapa
 * con el nombre de la llave como prefijo.
 * Si dentro de ese JSON hay otros JSON se aplica la misma operación de forma recursiva.
 */
fun Map<String, String>.collapseJsonMap(): Map<String, String> {
    val currentParams = mutableMapOf<String, String>()

    // Intentamos decodificar y aplanar los parámetros de todos los posibles objetos JSON
    forEach { (key, value) ->
        try {
            currentParams[ key ] = value
            val jsonObject = JsonParser.parseString( value ).asJsonObject

            currentParams.putAll(
                jsonObject.jsonObject2Map( key )
            )
        }catch (e: Exception) { /* No es un JSON */ }
    }

    return currentParams
}

/**
 * Analizamos el contenido de un Objeto JSON y lo convertimos en un mapa String, String
 * Si el contenido de una de sus llaves es un JSON, lo convertimos también y lo agregamos
 * al resultado final. De forma recursiva se aplica el mismo resultado a todos los otros
 * Objetos JSON dentro de este.
 */
fun JsonObject.jsonObject2Map(prefix: String = "" ): Map<String, String> {
    val currentMutableMap = mutableMapOf<String, String>()

    keySet().forEach { jeItem ->
        val je = get( jeItem )
        val prefixKey = "${prefix}_$jeItem"

        when {
            je.isJsonObject ->
                currentMutableMap.putAll(
                    je.asJsonObject.jsonObject2Map( jeItem )
                )

            je.isJsonArray ->
                je.asJsonArray.forEach {
                    if( it.isJsonObject )
                        currentMutableMap.putAll(
                            je.asJsonObject.jsonObject2Map( prefixKey )
                        )
                    else
                        currentMutableMap[ prefixKey ] = it.asString
                }

            je.isJsonNull ->
                currentMutableMap[ prefixKey ] = ""

            je.isJsonPrimitive ->
                try {
                    currentMutableMap[ prefixKey ] = je.asString

                    val jsonObject = JsonParser.parseString( je.asString ).asJsonObject

                    currentMutableMap.putAll(
                        jsonObject.jsonObject2Map( prefixKey )
                    )
                }catch (e: Exception) { /* No es un JSON */ }
        }
    }

    return currentMutableMap
}

/**
 * Convertimos un mapa de String, String en un Bundle para enviarlo como parámetros en los Intent
 */
fun Map<String, String>.toBundle(): Bundle =
    Bundle().apply {
        forEach {
            putString( it.key, it.value )
        }
    }

/**
 * Convertimos un Bundle en un mapa tipo <String, String>
 */
fun Bundle.toMap(): Map<String, String> =
    keySet().associateWith {
        getString( it ) ?:
        getInt( it ).toString()
    }

/**
 * Convertimos todas las llaves del mapa de snake_case a CamelCase
 */
fun Map<String, String>.camelCaseKeys( skipFirst: Boolean = false ) = mapKeys {
    it.key.replace("_", " ")
        .split(" ")
        .map {
            it.replaceFirstChar { c ->
                c.uppercaseChar()
            }
        }
        .joinToString()
        .replaceFirstChar { c ->
            if( ! skipFirst )
                c.lowercaseChar()
            c
        }
}

/**
 * Elimina los acentos de una cadena de texto
 */
fun String.cleanAccents(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")