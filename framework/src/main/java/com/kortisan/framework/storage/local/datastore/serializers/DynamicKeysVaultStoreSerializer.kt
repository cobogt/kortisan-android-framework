package com.kortisan.framework.storage.local.datastore.serializers
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.kortisan.framework.storage.local.datastore.DynamicKeysVaultStore
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object DynamicKeysVaultStoreSerializer: Serializer<DynamicKeysVaultStore> {
    override val defaultValue: DynamicKeysVaultStore
        get() = DynamicKeysVaultStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): DynamicKeysVaultStore {
        try {
            return DynamicKeysVaultStore.parseFrom( input )
        } catch (e: IOException) {
            throw CorruptionException("No se puede leer proto de DynamicKeysVaultStore", e)
        }
    }

    override suspend fun writeTo(t: DynamicKeysVaultStore, output: OutputStream) =
        t.writeTo( output )
}