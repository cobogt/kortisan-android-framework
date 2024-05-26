package com.kortisan.framework.storage.local.datastore.vaults

import android.content.Context
import com.kortisan.framework.storage.security.SecurityStrategy
import com.kortisan.framework.entities.Tokens
import com.kortisan.framework.storage.local.datastore.DataStoreSecureInterface
import com.kortisan.framework.storage.local.datastore.tokensVaultStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/
object TokensVaultStoreSecure: DataStoreSecureInterface<Tokens> {
    override fun getData(context: Context, strategy: SecurityStrategy): Flow<Tokens> =
        context.tokensVaultStore.data.map {
            Tokens(
                bearer            = strategy.decrypt( it.bearer ),
                bearerAge         = strategy.decrypt( it.bearerAge ),
                bearerExpireTime  = strategy.decrypt( it.bearerExpireTime ).toInt(),
                user              = strategy.decrypt( it.user ),
                userAge           = strategy.decrypt( it.userAge ),
                userExpireTime    = strategy.decrypt( it.userExpireTime ).toInt(),
                refresh           = strategy.decrypt( it.refresh ),
                refreshAge        = strategy.decrypt( it.refreshAge ),
                refreshExpireTime = strategy.decrypt( it.refreshExpireTime ).toInt(),
            )
        }

    override suspend fun setData(
        context: Context,
        inputData: Tokens,
        strategy: SecurityStrategy
    ) {
        context.tokensVaultStore.updateData {
            it.toBuilder()
                .apply {
                    bearer            = strategy.encrypt( inputData.bearer )
                    bearerAge         = strategy.encrypt( inputData.bearerAge )
                    bearerExpireTime  = strategy.encrypt( "${inputData.bearerExpireTime}" )
                    user              = strategy.encrypt( inputData.user )
                    userAge           = strategy.encrypt( inputData.userAge )
                    userExpireTime    = strategy.encrypt( "${inputData.userExpireTime}" )
                    refresh           = strategy.encrypt( inputData.refresh )
                    refreshAge        = strategy.encrypt( inputData.refreshAge )
                    refreshExpireTime = strategy.encrypt( "${inputData.refreshExpireTime}" )
                }
                .build()
        }
    }
}