package com.bro.pagingpicker.shared.gallery

import android.database.Cursor
import android.provider.MediaStore
import com.bro.pagingpicker.model.gallery.Image
import com.bro.pagingpicker.shared.util.closeSafely
import timber.log.Timber
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
    private val queryExecutor: QueryExecutor
) : ImageLoader {

    private var cursor: Cursor? = null

    private fun getCursor(): Cursor? {
        if (cursor == null) {
            cursor = queryExecutor.execute()
        }
        return cursor
    }

    override fun loadImage(fromPos: Int, loadSize: Int): List<Image> {
        if (getCursor() == null) {
            return ArrayList<Image>()
        }

        val images = ArrayList<Image>()
        val cur: Cursor = getCursor()!!
        Timber.d("### LocalImageLoader.loadImage.count : %s", cur.count)

        cur.moveToPosition(fromPos - 1)
        cur.let {
            for (i in 0 until loadSize) {
                if (!it.moveToNext()) {
                    break
                }
                val fileIdIndex = cur!!.getColumnIndex(MediaStore.Files.FileColumns._ID)
                val pathIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val data = cur.getString(pathIndex)
                val fileId = cur!!.getLong(fileIdIndex)

                images.add(Image(fileId, data))
                Timber.d("### LocalImageLoader.loadImage.path : %s", data)
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