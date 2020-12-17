package com.bro.pagingpicker.shared.gallery

import android.database.Cursor
import android.provider.MediaStore
import com.bro.pagingpicker.model.gallery.Image
import com.bro.pagingpicker.shared.util.closeSafely
import java.util.*

/**
 * Created by kyunghoon on 2020-12-18
 */
interface ImageLoader {
    fun loadImage(fromPos: Int, loadSize: Int): List<Image>
    fun reset()
    fun computeCount(): Int
}

class LocalImageLoader(
    private val cursorFactory: CursorFactory
) : ImageLoader {

    private var cursor: Cursor? = null

    private fun getCursor(): Cursor? {
        if (cursor == null) {
            cursor = cursorFactory.create()
        }
        return cursor
    }

    override fun loadImage(fromPos: Int, loadSize: Int): List<Image> {
        if (getCursor() == null) {
            return ArrayList<Image>()
        }

        val images = ArrayList<Image>()
        val cur: Cursor = getCursor()!!
        cur.moveToPosition(fromPos)
        cur.let {
            for (i in 0 until loadSize) {
                if (!it.moveToNext()) {
                    break
                }
                val dataIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val data = cur.getString(dataIndex);
                images.add(Image(data))
            }
        }
        return images
    }

    override fun computeCount(): Int {
        return getCursor()?.count ?: 0
    }

    override fun reset() {
        cursor?.closeSafely()
        cursor = null
    }

}