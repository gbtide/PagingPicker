package com.bro.pagingpicker.util

import android.view.View
import androidx.databinding.BindingAdapter
import com.bro.pagingpicker.widget.CustomSwipeRefreshLayout

/**
 * Created by kyunghoon on 2020-12-14
 */
@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("swipeRefreshColors")
fun setSwipeRefreshColors(swipeRefreshLayout: CustomSwipeRefreshLayout, colorResIds: IntArray) {
    // memo.
    // https://kotlinlang.org/docs/reference/functions.html#variable-number-of-arguments-varargs
    //
    // if we already have an array and want to pass its contents to the function, we use the spread operator (prefix the array with *):
    //
    // val a = arrayOf(1, 2, 3)
    // val list = asList(-1, 0, *a, 4)
    swipeRefreshLayout.setColorSchemeColors(*colorResIds)
}