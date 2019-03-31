package com.awimbawe.caye.recyclerviewsample.view.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.awimbawe.caye.recyclerviewsample.view.adapters.ItemAdapter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import com.awimbawe.caye.recyclerviewsample.R

/**
 * A [ItemTouchHelper.SimpleCallback] class responsible for reacting when a swipe is perform so it can call the adapter
 * in order to delete an item
 */
class SwipeToDeleteCallback(
    private var context : Context,
    private var adapter : ItemAdapter
): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private val icon: Drawable = ContextCompat.getDrawable(context, R.drawable.delete_icon)!!
    private val background : ColorDrawable = ColorDrawable(ContextCompat.getColor(context,R.color.colorPrimary))

    /**
     * Used only for up and down movements, not relevant but we must override
     */
    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        return false
    }

    /**
     * Used for horizontal movements, we fetch the position and delete the item
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.getAdapterPosition()
        adapter.deleteItem(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        var itemView = viewHolder.itemView
        val backgroundCornerOffset = 20 //so background is behind the rounded corners of itemView
        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        if (dX > 0) { // Swiping to the right
            moveRight(itemView, iconMargin, iconTop, iconBottom, dX, backgroundCornerOffset)
        } else if (dX < 0) { // Swiping to the left
            moveLeft(itemView, iconMargin, iconTop, iconBottom, dX, backgroundCornerOffset)
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        icon.draw(c)
    }

    private fun moveLeft(
        itemView: View,
        iconMargin: Int,
        iconTop: Int,
        iconBottom: Int,
        dX: Float,
        backgroundCornerOffset: Int
    ) {
        val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
        val iconRight = itemView.right - iconMargin
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

        background.setBounds(
            (itemView.right + dX).toInt() - backgroundCornerOffset,
            itemView.top, itemView.right, itemView.bottom
        )
    }

    private fun moveRight(
        itemView: View,
        iconMargin: Int,
        iconTop: Int,
        iconBottom: Int,
        dX: Float,
        backgroundCornerOffset: Int
    ) {
        val iconLeft = itemView.left + iconMargin + icon.intrinsicWidth
        val iconRight = itemView.left + iconMargin
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

        background.setBounds(
            itemView.left, itemView.top,
            (itemView.left + dX).toInt() + backgroundCornerOffset, itemView.bottom
        )
    }
}