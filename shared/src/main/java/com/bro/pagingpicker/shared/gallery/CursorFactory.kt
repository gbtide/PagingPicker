package com.bro.pagingpicker.shared.gallery

import android.database.Cursor

/**
 * Created by kyunghoon on 2020-12-14
 *
 * If your queries result in large amounts of data, (lots of columns and/or rows),
 * the cursor window will not be able to fit your entire query result and whenever you try to move the cursor position outside the cursor window,
 * a new file access occurs to repopulate the cursor window.
 * This can easily happen when querying the MediaProvider with a large projection on a device with thousands of images/videos.
 *
 */
interface CursorFactory {

    fun create():Cursor

}