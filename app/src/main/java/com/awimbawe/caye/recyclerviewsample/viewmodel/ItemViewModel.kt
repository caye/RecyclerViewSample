package com.awimbawe.caye.recyclerviewsample.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.awimbawe.caye.recyclerviewsample.model.AppDatabase
import com.awimbawe.caye.recyclerviewsample.model.entity.Item
import java.util.concurrent.Executors

/**
 * ViewModel holding the Items, its not needed right now but I add it in case we would want to continue building the app
 * from here
 */
class ItemViewModel(application: Application) : AndroidViewModel(application) {
    private val itemDAO = AppDatabase.getAppDataBase(application)!!.itemDAO()
    private val executorService = Executors.newSingleThreadExecutor()
    var item = Item()

    fun findAll(): LiveData<List<Item>> {
        return itemDAO.findAll()
    }

    fun insertItem(item: Item) {
        executorService.execute { itemDAO.insert(item) }
    }

    fun deleteItem(item: Item) {
        executorService.execute { itemDAO.delete(item) }
    }

    fun updateItem(item: Item) {
        executorService.execute{ itemDAO.update(item) }
    }
}