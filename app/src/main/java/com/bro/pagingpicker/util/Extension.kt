package com.bro.pagingpicker.util

import androidx.databinding.ViewDataBinding

/**
 * Created by kyunghoon on 2020-12-28
 */
class Extension {

    inline fun <T : ViewDataBinding> T.executeAfter(block: T.() -> Unit) {
        block()
        executePendingBindings()
    }

}