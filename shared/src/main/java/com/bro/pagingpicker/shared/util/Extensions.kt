package com.bro.pagingpicker.shared.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

/**
 * Created by kyunghoon on 2020-12-14
 */

fun <X, Y> LiveData<X>.map(body: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, body)
}