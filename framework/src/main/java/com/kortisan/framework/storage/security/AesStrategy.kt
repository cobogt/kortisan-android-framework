package com.kortisan.framework.storage.security
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.util.Base64
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AesStrategy: SecurityStrategy() {
    private val publicKeyRequest = ""
    private val publicKeyResponse = ""
    private val privateKey = NativeKeysAccess.getAesPrivateKey()
    override val transformation = "AES/CBC/PKCS5Padding"

    override fun encrypt(value: String): String {
        if(value.isEmpty()) return ""

        return process(
            value = value.toByteArray(),
            mode = Cipher.ENCRYPT_MODE,
            key = SecretKeySpec( "$publicKeyRequest${ dynamicKeys.aesPrivate }".toByteArray(), "AES"), // unsafeAesRequest
            parameters = IvParameterSpec( hex2Binary(privateKey) ),
            preProcess = { message, cipher ->
                cipher.doFinal( message )
            },
            postProcess = { encodeBase64(it, Base64.URL_SAFE) }
        )
    }

    override fun decrypt(value: String): String {
        if(value.isEmpty()) return ""

        return process(
            value = decodeBase64(value, Base64.URL_SAFE),
            mode = Cipher.DECRYPT_MODE,
            key = SecretKeySpec("$publicKeyRequest${ dynamicKeys.aesPublic }".toByteArray(), "AES"), // unsafeAesResponse
            parameters = IvParameterSpec( hex2Binary(privateKey) ),
            preProcess = { message, cipher ->
                cipher.doFinal( message )
            },
            postProcess = {
                String( it, Charset.defaultCharset() )
            }
        )
    }
}