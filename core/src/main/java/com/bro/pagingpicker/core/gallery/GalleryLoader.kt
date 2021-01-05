package com.bro.pagingpicker.core.gallery

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.bro.pagingpicker.core.util.closeSafely
import com.bro.pagingpicker.model.gallery.Image
import java.util.*

/**
 * Created by kyunghoon on 2020-12-18
 */
interface GalleryLoader {
    fun loadContents(fromPos: Int, loadSize: Int): List<Image>
    fun reset()
    fun computeCount(): Int
}

abstract class ContentsLoader(
    context: Context
) : GalleryLoader {
    private val queryExecutor: QueryExecutor

    init {
        queryExecutor = createQueryExecutor(context)
    }

    private var cursor: Cursor? = null

    abstract fun createQueryExecutor(context: Context): QueryExecutor

    protected fun getCursor(): Cursor? {
        if (cursor == null) {
            cursor = queryExecutor.execute()
        }
        return cursor
    }

    override fun computeCount(): Int {
        return getCursor()?.count ?: 0
    }

    override fun reset() {
        cursor?.closeSafely()
        cursor = null
    }

}

class ImageLoader(
    context: Context
) : ContentsLoader(context) {

    override fun createQueryExecutor(context: Context): QueryExecutor {
        return ImageQueryExecutor(context)
    }

    override fun loadContents(fromPos: Int, loadSize: Int): List<Image> {
        if (getCursor() == null) {
            return ArrayList<Image>()
        }

        val images = ArrayList<Image>()
        val cur: Cursor = getCursor()!!
//        Timber.d("### LocalImageLoader.loadImage.count : %s", cur.count)

        cur.moveToPosition(fromPos - 1)
        cur.let {
            for (i in 0 until loadSize) {
                if (!it.moveToNext()) {
                    break
                }
                val fileIdIndex = cur!!.getColumnIndex(MediaStore.Files.FileColumns._ID)
                val pathIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.DATA)

                val fileId = cur!!.getLong(fileIdIndex)
                val path = cur.getString(pathIndex)

                images.add(Image(fileId, path))
//                Timber.d("### LocalImageLoader.loadImage.path : %s", data)
            }
        }
        return images
    }

}