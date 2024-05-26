package com.kortisan.framework.storage.security
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

/**
 * Estrategia que no implementa ningún tipo de seguridad
 */
object NoSecurityStrategy: SecurityStrategy() {
    override val transformation: String = ""

    override fun encrypt(value: String): String {
        return value
    }

    override fun decrypt(value: String): String {
        return value
    }
}