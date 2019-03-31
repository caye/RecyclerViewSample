package com.awimbawe.caye.recyclerviewsample

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Toast
import com.awimbawe.caye.recyclerviewsample.view.adapters.ItemAdapter
import com.awimbawe.caye.recyclerviewsample.model.entity.Item
import com.awimbawe.caye.recyclerviewsample.model.entity.extractDate
import com.awimbawe.caye.recyclerviewsample.utils.ApiInterface
import com.awimbawe.caye.recyclerviewsample.view.helpers.SwipeToDeleteCallback
import com.awimbawe.caye.recyclerviewsample.viewmodel.ItemViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import com.awimbawe.caye.recyclerviewsample.view.dialogs.ConfirmDeletionDialogFragment


class MainActivity : AppCompatActivity(), ItemAdapter.OnItemClickListener {
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var itemListAdapter : ItemAdapter
    private lateinit var itemViewModel: ItemViewModel
    private var data : MutableList<Item> = mutableListOf()

    //api in order to make network calls
    private val api by lazy {
        ApiInterface.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coordinatorLayout = findViewById<CoordinatorLayout>(R.id.coordinator_layout);

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
        viewManager = LinearLayoutManager(this)
        itemListAdapter = ItemAdapter(this,data,this)
        recyclerView = findViewById<RecyclerView>(R.id.item_list_recyclerview).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = itemListAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        itemViewModel!!.findAll().observe(this, Observer { items: Collection<Item>? -> itemListAdapter.setData(getListWithDates(items!!)) })
        var itemTouchHelper = ItemTouchHelper(
            SwipeToDeleteCallback(
                this,
                itemListAdapter
            )
        );
        itemTouchHelper.attachToRecyclerView(recyclerView)

        requestItems()
    }

    /**
     * Makes a request to the mockaroo API in order to retrieve the list of items, once the list is retrieved it'll
     * update the dataset in the adapter
     */
    private fun requestItems() {
        api.requestItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { items ->
                    itemListAdapter.setData(getListWithDates(items))
                    Toast.makeText(this, "Test Results updated", Toast.LENGTH_SHORT).show()
                },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
            )
    }

    override fun itemClicked(item: Item) {
        var confirmDeletionDialog = ConfirmDeletionDialogFragment()
        confirmDeletionDialog.adapter = itemListAdapter
        confirmDeletionDialog.item = item
        confirmDeletionDialog.show(supportFragmentManager,"confirm_deletion_dialog")
    }

    /**
     * Gets a list of items retrieved from mockaroo and returns the list with all the passed items plus a
     * {@link}Item.ItemType object added at the begining of each month
     */
    private fun getListWithDates(items: Collection<Item>) : MutableList<Item> {
        val result = mutableListOf<Item>()
        val dates = TreeSet<String>()
        for(item in items) {
            var month = item.month.extractDate()
            if(!dates.contains(month)) {
                dates.add(month)
                var date = Item()
                date.type = Item.ItemType.DATE
                date.month = item.month
                result.add(date)
            }

            result.add(item)
        }

        return result
    }

}
