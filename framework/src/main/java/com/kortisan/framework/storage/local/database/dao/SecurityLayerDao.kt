package com.kortisan.framework.storage.local.database.dao


import com.kortisan.framework.storage.security.SecurityStrategy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

open class SecurityLayerDao<T> {
    open fun encryptModel(inputData: T, strategy: SecurityStrategy): T = inputData
    open fun decryptModel(inputData: T, strategy: SecurityStrategy): T = inputData

    open fun safeInsert(inputData: T, strategy: SecurityStrategy) { /* Inserción segura */ }
    open fun safeSelect(inputData: T, strategy: SecurityStrategy): T = inputData

    fun safeSelect(inputData: Flow<T>, strategy: SecurityStrategy): Flow<T> =
        inputData.transform { safeSelect(it, strategy) }

    fun safeSelectList(inputData: Flow<List<T>>, strategy: SecurityStrategy): Flow<List<T>> =
        inputData.transform { list -> list.map { safeSelect(it, strategy) } }
}
