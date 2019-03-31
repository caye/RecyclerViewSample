package com.awimbawe.caye.recyclerviewsample.view.adapters

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.awimbawe.caye.recyclerviewsample.MainActivity
import com.awimbawe.caye.recyclerviewsample.R
import com.awimbawe.caye.recyclerviewsample.model.entity.Item
import com.awimbawe.caye.recyclerviewsample.model.entity.extractDate
import android.support.design.widget.Snackbar

/**
 * Extension function used to trim text in an acceptable manner so they are shown as "textCutted ..."
 */
private fun TextView.abbreviate(text: String,maxLegth: Int) {
    var result = text

    if(result.length > maxLegth) {
        result = text.substring(0,maxLegth - 3) + "..."
    }

    this.text = result
}

/**
 * A [RecyclerView.Adapter] class responsible for
 */
class ItemAdapter(
    private val context : Context,
    private var data: MutableList<Item>,
    private val onItemClicked: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lastDeletedItem : Pair<Int,Item>? = null

    /**
     * Interface implemented by the [MainActivity] so we can let it know when an item has been clicked
     */
    interface OnItemClickListener {
        fun itemClicked(item: Item)
    }

    // Create new view holders so layout only needs to be inflated once
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            1 -> return DateHeaderViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.header_layout, parent, false))
            else -> return ItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout, parent, false))
        }
    }

    // returns the type of the view, if its ItemType.Data or ItemType.Date
    override fun getItemViewType(position: Int): Int {
        return data.get(position).type.value
    }

    // Replace the contents of a view holders
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            1 -> {
                var viewHolder = holder as DateHeaderViewHolder
                viewHolder.bind(data.toMutableList()!![position])
            }
            else -> {
                var viewHolder = holder as ItemViewHolder
                viewHolder.bind(data.toMutableList()!![position],onItemClicked)
            }
        }

    }

    // Return the size of your data
    override fun getItemCount() = data.size

    /**
     * Sets a given [MutableList] to be the data to be rendered by this adapter
     */
    fun setData(newData: MutableList<Item>) {
        if (data != null) {
            val testDiffCallback = ItemListDiffCallback(data!!, newData)
            val diffResult = DiffUtil.calculateDiff(testDiffCallback)

            data!!.clear()
            data!!.addAll(newData)
            diffResult.dispatchUpdatesTo(this)
        } else {
            // first initialization
            data = newData
        }
    }

    /**
     * Given a position, deletes an item in the list and shows the snackbar to undo it
     */
    fun deleteItem(position: Int) {
        lastDeletedItem = Pair(position, data[position])
        data.removeAt(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    /**
     * Given an [Item], finds its position deletes the item in the list and shows the snackbar to undo it
     */
    fun deleteItem(item: Item) {
        var position = data.indexOf(item)
        lastDeletedItem = Pair(position,data[position])
        data.removeAt(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    /**
     * Shows an snackbar in order to be available to undo the deletion
     */
    private fun showUndoSnackbar() {
        val activity = context as MainActivity
        var view = activity.findViewById<CoordinatorLayout>(R.id.coordinator_layout)
        val snackbar = Snackbar.make(
            view, R.string.item_deleted,
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(R.string.undo_deletion) { undoDeleteItem() }
        snackbar.show()
    }

    /**
     * Restores the last item deleted
     */
    private fun undoDeleteItem() {
        if(lastDeletedItem != null) {
            data.add(lastDeletedItem!!.first,lastDeletedItem!!.second)
            notifyItemInserted(lastDeletedItem!!.first);
        }
    }

    /**
     * ViewHolder for the items in the list
     */
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemImage : ImageView
        private val itemTitle : TextView
        private val itemAmount : TextView
        private val itemCurrency : TextView
        private val itemStatus : TextView
        private val itemArrowIcon : ImageView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemAmount = itemView.findViewById(R.id.item_amount)
            itemCurrency = itemView.findViewById(R.id.item_currency)
            itemStatus = itemView.findViewById(R.id.item_status)
            itemArrowIcon = itemView.findViewById(R.id.item_arrow_icon)
        }

        fun bind(item: Item?, listener : OnItemClickListener) {
            if (item != null) {
                itemTitle.abbreviate(item.title,30)
                itemAmount.text = item.amount.toString()
                itemCurrency.text = item.currency
                itemStatus.text = item.status
            }

            itemView.setOnClickListener { listener.itemClicked(item!!) }
        }

    }

    /**
     * ViewHolder for the dates headers in the list
     */
    inner class DateHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerMonth : TextView = itemView.findViewById(R.id.header_month)
        private val headerChecked : ImageView = itemView.findViewById(R.id.header_checked)

        fun bind(item: Item?) {
            if (item != null) {
                headerMonth.text = item.month.extractDate()

                if ((1..10).shuffled().first() % 2 == 0)  headerChecked.visibility = View.VISIBLE  else headerChecked.visibility = View.GONE
            }
        }
    }

    /**
     * Checks wether a [Item] List has been changed
     */
    inner class ItemListDiffCallback(private val oldItems: List<Item>, private val newItems: List<Item>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItems.size
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].id === newItems[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }
    }
}
