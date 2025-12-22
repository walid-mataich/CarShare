package com.example.frontend.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LastItemBottomMarginDecoration(private val lastItemExtraMarginDp: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (position == itemCount - 1) {
            outRect.bottom = lastItemExtraMarginDp.dpToPx(view.context)
        }
    }
}

// Extension pour convertir dp -> px
fun Int.dpToPx(context: Context): Int =
    (this * context.resources.displayMetrics.density).toInt()
