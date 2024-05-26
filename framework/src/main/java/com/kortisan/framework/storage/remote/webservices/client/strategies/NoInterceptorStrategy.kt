package com.kortisan.framework.storage.remote.webservices.client.strategies
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import okhttp3.OkHttpClient

/**
 * Estrategia libre de interceptores
 */
object NoInterceptorStrategy: ClientBuilderStrategy() {
    override val baseUrl: String = ""

    override val client: OkHttpClient
        get() = _client
            .build()
}