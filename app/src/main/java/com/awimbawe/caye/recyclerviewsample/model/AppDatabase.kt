package com.awimbawe.caye.recyclerviewsample.model

import android.arch.persistence.room.*
import android.content.Context
import com.awimbawe.caye.recyclerviewsample.model.converter.DateTypeConverter
import com.awimbawe.caye.recyclerviewsample.model.converter.ItemTypeConverter
import com.awimbawe.caye.recyclerviewsample.model.dao.ItemDAO
import com.awimbawe.caye.recyclerviewsample.model.entity.Item

/**
 * Database class that serves for access to the DAOs
 */
@Database(entities = [Item::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class,ItemTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDAO() : ItemDAO

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "RecyclerViewSampleDB").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}