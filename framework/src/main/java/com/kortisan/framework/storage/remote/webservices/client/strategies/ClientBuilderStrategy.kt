package com.kortisan.framework.storage.remote.webservices.client.strategies
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

sealed class ClientBuilderStrategy {
    abstract val baseUrl: String
    abstract val client: OkHttpClient

    // 15 segundos para que falle la petición
    private val timeOut: Long = 15

    // Cliente base para todas las estrategias
    internal val _client = OkHttpClient.Builder()
// En caso de ser necesario limitar la cantidad de peticiones simultaneas a 1
//        .dispatcher(Dispatcher().apply {
//            maxRequests = 1
//            maxRequestsPerHost = 1
//        })
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .connectTimeout(timeOut, TimeUnit.SECONDS)
        .callTimeout(timeOut, TimeUnit.SECONDS)
        .readTimeout(timeOut, TimeUnit.SECONDS)
        .writeTimeout(timeOut, TimeUnit.SECONDS)
}