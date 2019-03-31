package com.awimbawe.caye.recyclerviewsample.model.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.awimbawe.caye.recyclerviewsample.model.entity.Item

/**
 * Called in order to allow access to the room database
 */
@Dao
interface ItemDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<Item>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)

    @Query("SELECT * FROM item")
    fun findAll(): LiveData<List<Item>>

    @Query("SELECT * FROM item WHERE id == :id")
    fun findById(id:String): Item
}