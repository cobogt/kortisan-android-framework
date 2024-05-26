package com.kortisan.framework.storage.local.datastore.vaults
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.content.Context
import com.kortisan.framework.storage.local.datastore.DynamicKeysVaultStore
import com.kortisan.framework.storage.security.SecurityStrategy
import com.kortisan.framework.entities.DynamicKeys
import com.kortisan.framework.storage.local.datastore.DataStoreSecureInterface
import com.kortisan.framework.storage.local.datastore.dynamicKeysVaultStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DynamicKeysVaultStoreSecure: DataStoreSecureInterface<DynamicKeys> {
    override fun getData(context: Context, strategy: SecurityStrategy): Flow<DynamicKeys> =
        context.dynamicKeysVaultStore.data.map { dynamicKeysVaultStore: DynamicKeysVaultStore ->
            DynamicKeys(
                aesPrivate = strategy.decrypt( dynamicKeysVaultStore.aesUnsafePrivate ),
                aesPublic = strategy.decrypt( dynamicKeysVaultStore.aesUnsafePublic ),
            )
        }

    override suspend fun setData(
        context: Context,
        inputData: DynamicKeys,
        strategy: SecurityStrategy
    ) {
        context.dynamicKeysVaultStore.updateData { dynamicKeysVaultStore: DynamicKeysVaultStore ->
            dynamicKeysVaultStore.toBuilder()
                .setAesUnsafePrivate( strategy.encrypt( inputData.aesPrivate ))
                .setAesUnsafePublic( strategy.encrypt( inputData.aesPublic ))
                .build()
        }
    }
}