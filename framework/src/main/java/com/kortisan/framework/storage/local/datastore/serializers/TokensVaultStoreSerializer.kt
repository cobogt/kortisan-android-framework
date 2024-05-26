package com.kortisan.framework.storage.local.datastore.serializers

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.kortisan.framework.storage.local.datastore.TokensVaultStore
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object TokensVaultStoreSerializer: Serializer<TokensVaultStore> {
    override val defaultValue: TokensVaultStore
        get() = TokensVaultStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TokensVaultStore {
        try {
            return TokensVaultStore.parseFrom( input )
        } catch (e: IOException) {
            throw CorruptionException("No se puede leer proto de TokensVaultStore", e)
        }
    }

    override suspend fun writeTo(t: TokensVaultStore, output: OutputStream) = t.writeTo( output )
}