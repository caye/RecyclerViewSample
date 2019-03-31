package com.awimbawe.caye.recyclerviewsample.model.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.awimbawe.caye.recyclerviewsample.model.converter.ItemTypeConverter
import io.reactivex.annotations.NonNull
import java.text.SimpleDateFormat
import java.util.*

/**
 * Extension function used in order to provide a human readable format for a Date so it can be displayed in the list
 */
fun Date.extractDate() : String {
    var currentDate = Date()
    val cal = Calendar.getInstance()
    cal.set(Calendar.DAY_OF_YEAR,1)
    cal.set(Calendar.HOUR_OF_DAY,0)
    cal.set(Calendar.MINUTE,0)
    cal.set(Calendar.SECOND,0)

    if(this.compareTo(cal.time) < 0) {
        return SimpleDateFormat("MMMM yyyy").format(this.time)
    }

    return SimpleDateFormat("MMMM").format(this.time)
}


/**
 * This class represents an item downloaded from the mockaroo API and holds the data that is going to be represented in
 * the list
 *
 * The model is ready for use with Room for persistance
 */
@Entity(tableName = "item")
data class Item (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int? = null,
    var month : Date,
    var icon: String,
    var title : String,
    var amount : Double,
    var currency : String,
    var status : String,
    @TypeConverters(ItemTypeConverter::class)
    var type : ItemType //Whether this is a Date type or a Data type, this is used by the adapter in order to know what ViewHolder to use

) { @Ignore
constructor():this(null, Date(),"", "", 0.0, "", "", ItemType.DATA)

    enum class ItemType(val value: Int) {
        DATA(0), DATE(1)
    }
}