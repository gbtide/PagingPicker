package com.bro.pagingpicker.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by kyunghoon on 2020-12-22
 */
class GridDecoration(val spanCount:Int, val rowOffset:Int, val columnOffset:Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)


    }
}