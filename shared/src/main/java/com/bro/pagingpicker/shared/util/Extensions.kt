package com.bro.pagingpicker.shared.util

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

/**
 * Created by kyunghoon on 2020-12-14
 */

/**
 * Helper to force a when statement to assert all options are matched in a when statement.
 *
 * By default, Kotlin doesn't care if all branches are handled in a when statement. However, if you
 * use the when statement as an expression (with a value) it will force all cases to be handled.
 *
 * This helper is to make a lightweight way to say you meant to match all of them.
 *
 * Usage:
 *
 * ```
 * when(sealedObject) {
 *     is OneType -> //
 *     is AnotherType -> //
 * }.checkAllMatched
 */
val <T> T.checkAllMatched: T
    get() = this

fun <X, Y> LiveData<X>.map(body: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, body)
}

fun Cursor.closeSafely() {
    this?.let {
        if (!this.isClosed) {
            this.close()
        }
    }
}