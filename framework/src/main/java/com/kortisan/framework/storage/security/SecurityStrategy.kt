package com.kortisan.framework.storage.security
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.os.StrictMode
import android.util.Base64
import com.kortisan.framework.FrameworkApplicationBinding
import com.kortisan.framework.storage.local.datastore.vaults.DynamicKeysVaultStoreSecure
import com.kortisan.framework.entities.DynamicKeys
import kotlinx.coroutines.*
import org.apache.commons.net.ntp.NTPUDPClient
import java.net.InetAddress
import java.security.Key
import java.security.spec.AlgorithmParameterSpec
import java.text.Normalizer
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher

sealed class SecurityStrategy {
    @OptIn(DelicateCoroutinesApi::class)
    companion object {
        internal var dynamicKeys = DynamicKeys()

        var networkTimeMillis: Long = 0L
        var networkTimeDifferenceMillis: Long = 0L
        var networkTimeFetchMillis: Long = 0L

        var dispatcher = Dispatchers.IO

        init {
            GlobalScope.launch( dispatcher ) {
                DynamicKeysVaultStoreSecure.getData(
                    FrameworkApplicationBinding.appContext,
                    AesStrategy
                )
                .collect { dynamicKeys = it }
            }
        }
    }

    abstract val transformation: String

    internal open fun <T, A>process(
        value:       T,
        mode:        Int,
        key:         Key,
        parameters:  AlgorithmParameterSpec?,
        preProcess:  (T, Cipher) -> ByteArray,
        postProcess: (ByteArray)  -> A
    ): A {
        val cipher = Cipher.getInstance(transformation)

        if(parameters == null)
            cipher.init(mode, key)
        else
            cipher.init(mode, key, parameters)

        return postProcess (
            preProcess(
                value,
                cipher
            )
        )
    }

    abstract fun encrypt(value: String): String
    abstract fun decrypt(value: String): String

    // Comprueba dos hashes para ver si coinciden
    open fun check(value1: String, value2: String): Boolean = value1.compareTo(value2) == 0

    fun decodeBase64(value: String, flags: Int = Base64.DEFAULT): ByteArray =
        Base64.decode(value, flags)

    fun encodeBase64(value: ByteArray, flags: Int = Base64.DEFAULT): String =
        Base64.encodeToString(value, flags)

    // Convierte un string a un arreglo de bytes
    fun hex2Binary(key: String): ByteArray {
        val binary = ByteArray(key.length / 2)
        for (i in binary.indices) {
            binary[i] = Integer.parseInt(key.substring(2 * i, 2 * i + 2), 16).toByte()
        }
        return binary
    }

    // Elimina acentos
    fun unaccent( value: String ): String = "\\p{InCombiningDiacriticalMarks}+".toRegex()
        .replace( Normalizer.normalize(value, Normalizer.Form.NFD), "" )

    // Limpia la cadena para poder encriptar/decriptar con AES
    // ( Equivalente a Cypher.cleanEncryptSafeUrl )
    fun clean(value: String): String = value
        .replace("=", "")
        .replace("\\n", "")
        .trim { it <= ' ' }

    fun updateNetworkTime() = updateNewtworkTimeServer("time.google.com")

    fun updateNewtworkTimeServer( timeServer: String ) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val timeClient = NTPUDPClient()
        timeClient.defaultTimeout = 20000

        val timeInfo = timeClient.getTime(
            InetAddress.getByName(timeServer)
        )

        val serverTimeMillis = TimeUnit.MILLISECONDS.toMillis(
            timeInfo.message.transmitTimeStamp.time
        )

        val systemTimeMillis = System.currentTimeMillis()

        networkTimeMillis           = serverTimeMillis
        networkTimeDifferenceMillis = serverTimeMillis - systemTimeMillis
        networkTimeFetchMillis      = systemTimeMillis
    }

/*
            try {
            } catch (e: IOException) {
                throw RuntimeException(e)
            } catch (e: NoSuchAlgorithmException | NoSuchPaddingException) {
                throw RuntimeException(e)
            } catch (e: InvalidKeyException) {
                throw RuntimeException(e)
            } catch (e: IllegalBlockSizeException) {
                throw RuntimeException(e)
            } catch (e: BadPaddingException) {
                throw RuntimeException(e)
            }
 */
}