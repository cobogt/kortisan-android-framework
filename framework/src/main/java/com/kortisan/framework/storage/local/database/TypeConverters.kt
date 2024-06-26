package com.kortisan.framework.storage.local.database

import androidx.room.TypeConverter
import java.util.*

class TypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? =
        value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? =
        date?.time

    @TypeConverter
    fun arrayIntToString(intList: ArrayList<Int>?): String? =
        intList?.joinToString(";")

    @TypeConverter
    fun arrayStringToString(array: ArrayList<String>?): String? =
        array?.joinToString(",")

    @TypeConverter
    fun stringToArrayInt(stringList: String?): ArrayList<Int> =
        stringList?.let {
            if(it.isNotEmpty())
                it.split(";")
                    .mapTo(ArrayList<Int>()) { split -> split.toInt() }
            else null
        } ?: arrayListOf()

    @TypeConverter
    fun stringToArrayString(string: String?): ArrayList<String> =
        string?.let { s ->
            if(s.isNotEmpty())
                return s.split(",").mapTo(ArrayList<String>()) { it }
            else null
        } ?: arrayListOf()
}
