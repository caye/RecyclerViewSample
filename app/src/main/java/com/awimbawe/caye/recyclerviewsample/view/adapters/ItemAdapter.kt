package com.awimbawe.caye.recyclerviewsample.view.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.awimbawe.caye.recyclerviewsample.R
import com.awimbawe.caye.recyclerviewsample.model.entity.Item
import com.awimbawe.caye.recyclerviewsample.model.entity.extractDate


class ItemAdapter(
    private var dataset: MutableList<Item>,
    private val onItemClicked: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            1 -> return DateViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.header_layout, parent, false))
            else -> return ItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout, parent, false))
        }
        // set the view's size, margins, paddings and layout parameters

    }

    override fun getItemViewType(position: Int): Int {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return dataset.get(position).type.value
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        when(holder.itemViewType) {
            1 -> {
                var viewHolder = holder as DateViewHolder
                viewHolder.bind(dataset.toMutableList()!![position])
            }
            else -> {
                var viewHolder = holder as ItemViewHolder
                viewHolder.bind(dataset.toMutableList()!![position],onItemClicked)
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.size

    fun setData(newData: MutableList<Item>) {
        if (dataset != null) {
            val testDiffCallback = TestDiffCallback(dataset!!, newData)
            val diffResult = DiffUtil.calculateDiff(testDiffCallback)

            dataset!!.clear()
            dataset!!.addAll(newData)
            diffResult.dispatchUpdatesTo(this)
        } else {
            // first initialization
            dataset = newData
        }
    }

    fun deleteItem(position: Int) {

    }

    interface OnItemClickListener {
        fun itemClicked(item: Item)
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
     * ViewHolder for the dates in the list
     */
    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateMonth : TextView
        private val dateChecked : ImageView

        init {
            dateMonth = itemView.findViewById(R.id.header_month)
            dateChecked = itemView.findViewById(R.id.header_checked)
        }

        fun bind(item: Item?) {
            if (item != null) {
                dateMonth.text = item.month.extractDate()

                if ((1..10).shuffled().first() % 2 == 0)  dateChecked.visibility = View.VISIBLE  else dateChecked.visibility = View.GONE
            }
        }
    }


    inner class TestDiffCallback(private val oldTests: List<Item>, private val newTests: List<Item>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldTests.size
        }

        override fun getNewListSize(): Int {
            return newTests.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldTests[oldItemPosition].id === newTests[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldTests[oldItemPosition] == newTests[newItemPosition]
        }
    }
}

private fun TextView.abbreviate(text: String,maxLegth: Int) {
    var result = text

    if(result.length > maxLegth) {
        result = text.substring(0,maxLegth - 3) + "..."
    }

    this.text = result
}
