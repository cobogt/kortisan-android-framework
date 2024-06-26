package com.kortisan.framework.storage.remote.webservices.client
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import com.kortisan.framework.storage.remote.webservices.client.interceptors.apiInterfaces.AuthenticationApiInterface
import com.kortisan.framework.storage.remote.webservices.client.strategies.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())

    fun <T> buildService( service: Class<T>, strategy: ClientBuilderStrategy
        = NoInterceptorStrategy ): T {
            return retrofit
                .baseUrl( strategy.baseUrl )
                .client( strategy.client )
                .build()
                .create( service )
    }

    fun authenticationService(): AuthenticationApiInterface =
       buildService(
           AuthenticationApiInterface::class.java,
           NoInterceptorStrategy
        )
}
