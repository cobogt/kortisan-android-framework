package com.kortisan.framework.storage.remote.webservices.client.interceptors.application

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 06/01/23.
 * * * * * * * * * * **/
/**
 * Intercepta la petición y agrega el código HTTP a la respuesta obtenida
 */
class HttpCodeInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Obtenemos la petición
        val request = chain.request()

        // Procesamos la petición y obtenemos la respuesta
        val response = chain.proceed(request)

        val contentType = response.headers["Content-Type"]
        // Si la respuesta es de tipo JSON y tiene un código de éxito (2XX)
        if ( contentType == "application/json" ) {
            // Obtenemos el cuerpo de la respuesta en formato JSON
            response.body?.string()?.also { body ->
                val jsonObject = JSONObject(body)

                // Añadimos el código HTTP como un parámetro
                jsonObject.put("httpCode", response.code)

                // Convertimos el JSON modificado a una cadena y creamos un nuevo
                //   cuerpo para la respuesta.
                val modifiedBody = jsonObject.toString().toResponseBody(
                    body.toMediaType()
                )

                // Creamos una nueva respuesta con el nuevo cuerpo
                return response.newBuilder().body( modifiedBody ).build()
            }
        }

        // Si la respuesta no es de tipo JSON o no tiene un código de éxito, retornamos
        // la respuesta original.
        return response
    }
}
