package com.kortisan.framework.storage.local.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUnsafe(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUnsafe(vararg obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUnsafe(list: List<T>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAllUnsafe(list: List<T>)

    @Delete
    fun delete(obj: T)

    @Update
    fun updateUnsafe(obj: T): Int
}
