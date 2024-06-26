package com.kortisan.framework.storage.local.datastore
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.kortisan.framework.storage.local.datastore.serializers.DynamicKeysVaultStoreSerializer
import com.kortisan.framework.storage.local.datastore.serializers.TokensVaultStoreSerializer

val Context.dynamicKeysVaultStore: DataStore<DynamicKeysVaultStore> by dataStore(
    fileName = "dynamicKeysDataStore.pb",
    serializer = DynamicKeysVaultStoreSerializer
)

val Context.tokensVaultStore: DataStore<TokensVaultStore> by dataStore(
    fileName = "tokensVaultStore.pb",
    serializer = TokensVaultStoreSerializer
)
