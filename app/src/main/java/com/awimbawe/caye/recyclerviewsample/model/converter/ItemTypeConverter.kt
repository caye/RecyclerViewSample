package com.awimbawe.caye.recyclerviewsample.model.converter

import android.arch.persistence.room.TypeConverter
import com.awimbawe.caye.recyclerviewsample.model.entity.Item

class ItemTypeConverter {
    @TypeConverter
    fun toItemType(type: Int): Item.ItemType {
        return if (type == Item.ItemType.DATE.value) {
            Item.ItemType.DATE
        } else if (type == Item.ItemType.DATA.value) {
            Item.ItemType.DATA
        } else {
            throw IllegalArgumentException("Could not recognize ItemType")
        }
    }

    @TypeConverter
    fun toInt(type: Item.ItemType): Int? {
        return type.value
    }
}