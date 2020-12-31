package com.bro.pagingpicker.core.result

/**
 * Created by kyunghoon on 2020-12-31
 */
sealed class ResultDataState {

    object Exist : ResultDataState()
    object Empty : ResultDataState()

    override fun toString(): String {
        return when (this) {
            is Exist -> "Exist"
            is Empty -> "Empty"
        }
    }

}