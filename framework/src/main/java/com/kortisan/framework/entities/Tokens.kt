package com.kortisan.framework.entities

import org.joda.time.DateTime

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

data class Tokens(
    val bearer:           String,
    val bearerAge:        String,
    val bearerExpireTime: Int,

    val user:             String,
    val userAge:          String,
    val userExpireTime:   Int,

    val refresh:          String,
    val refreshAge:       String,
    val refreshExpireTime:Int,
) {
    val isBearerTokenValid: Boolean
        get() = bearer.isNotBlank()
                && bearerAge.isNotBlank()
                && DateTime.parse( bearerAge ).isAfterNow

    val isAccessTokenValid: Boolean
        get() = user.isNotBlank()
                && userAge.isNotBlank()
                && DateTime.parse( userAge ).isAfterNow

    val isRefreshTokenValid: Boolean
        get() = refresh.isNotBlank()
                && refreshAge.isNotBlank()
                && DateTime.parse( refreshAge ).isAfterNow
}
