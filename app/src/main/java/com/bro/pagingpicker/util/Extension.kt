package com.bro.pagingpicker.util

import androidx.databinding.ViewDataBinding
import com.bro.pagingpicker.core.util.checkAllMatched

/**
 * Created by kyunghoon on 2020-12-28
 *
 * memo. 확장 함수 정의할 때 class Extension 으로 감싸면 안되는 것 같다.
 */

inline fun <T : ViewDataBinding> T.executeAfter(block: T.() -> Unit) {
    block()
    executePendingBindings()
}