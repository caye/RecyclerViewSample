package com.awimbawe.caye.recyclerviewsample.model.converter

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Converter for the Date type to be stored into the Room database
 */
class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}