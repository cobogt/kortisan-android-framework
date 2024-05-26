package com.kortisan.framework.storage.local.datastore
/** * * * * * * * * *
 * Project KoreFrame
 * Created by Jacobo G Tamayo on 30/12/22.
* * * * * * * * * * **/

import android.content.Context
import com.kortisan.framework.storage.security.SecurityStrategy

import kotlinx.coroutines.flow.Flow

/**
 * Interfaz para obtener y leer la información de un datastore utilizando una estrategia de
 * seguridad como intermediaria.
 *
 * El tipo T es antes de la encriptación y/o después de la des encriptación.
 * Normalmente el tipo T es una entidad de la aplicación.
 */
interface DataStoreSecureInterface<T> {
    fun getData( context: Context, strategy: SecurityStrategy): Flow<T>
    suspend fun setData( context: Context, inputData: T, strategy: SecurityStrategy)
}