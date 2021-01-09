package com.bro.pagingpicker.core.gallery

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.bro.pagingpicker.core.util.closeSafely
import com.bro.pagingpicker.model.gallery.GalleryItem
import com.bro.pagingpicker.model.gallery.Image
import com.bro.pagingpicker.model.gallery.Video
import java.util.*

/**
 * Created by kyunghoon on 2020-12-18
 */
interface GalleryLoader<T> {
    @WorkerThread
    fun loadContents(fromPos: Int, loadSize: Int): List<T>

    fun reset()

    @WorkerThread
    fun computeCount(): Int
}

abstract class ContentsLoader<T>(
    context: Context
) : GalleryLoader<T> {
    private val queryExecutor: QueryExecutor

    init {
        queryExecutor = createQueryExecutor(context)
    }

    private var cursor: Cursor? = null

    abstract fun createQueryExecutor(context: Context): QueryExecutor

    @WorkerThread
    protected fun executeQueryIfNotDoneBefore(): Cursor? {
        if (cursor == null) {
            // reset 전까지 재활용
            cursor = queryExecutor.execute()
        }
        return cursor
    }

    override fun computeCount(): Int {
        return executeQueryIfNotDoneBefore()?.count ?: 0
    }

    override fun reset() {
        cursor?.closeSafely()
        cursor = null
    }

}

class ImageLoader(
    context: Context
) : ContentsLoader<Image>(context) {

    override fun createQueryExecutor(context: Context): QueryExecutor {
        return ImageQueryExecutor(context)
    }

    @WorkerThread
    override fun loadContents(fromPos: Int, loadSize: Int): List<Image> {
        // memo. query method 가 nullable
        val cur: Cursor = executeQueryIfNotDoneBefore() ?: return ArrayList<Image>()

        val images = ArrayList<Image>()
        cur.moveToPosition(fromPos - 1)
        cur.let {
            for (i in 0 until loadSize) {
                if (!it.moveToNext()) {
                    break
                }
                // find index
                val idIndex = cur.getColumnIndex(MediaStore.Files.FileColumns._ID)
                val filePathIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val mimeTypeIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)

                // get
                val id = cur.getLong(idIndex)
                val filePath = cur.getString(filePathIndex)
                val mimeType = cur.getString(mimeTypeIndex)
                images.add(
                    Image(
                        _id = id,
                        _filePath = filePath,
                        _mimeType = mimeType
                    )
                )
            }
        }
        return images
    }

}

class VideoLoader(
    context: Context
) : ContentsLoader<Video>(context) {

    override fun createQueryExecutor(context: Context): QueryExecutor {
        return VideoQueryExecutor(context)
    }

    @WorkerThread
    override fun loadContents(fromPos: Int, loadSize: Int): List<Video> {
        val cur: Cursor = executeQueryIfNotDoneBefore() ?: return ArrayList<Video>()

        val videos = ArrayList<Video>()
        cur.moveToPosition(fromPos - 1)
        cur.let {
            for (i in 0 until loadSize) {
                if (!it.moveToNext()) {
                    break
                }
                // find index
                val idIndex = cur.getColumnIndex(MediaStore.Files.FileColumns._ID)
                val filePathIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val mimeTypeIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
                val durationIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.DURATION)

                // get
                val id = cur.getLong(idIndex)
                val filePath = cur.getString(filePathIndex)
                val mimeType = cur.getString(mimeTypeIndex)
                val duration = cur.getLong(durationIndex)
                videos.add(
                    Video(
                        _id = id,
                        _filePath = filePath,
                        _mimeType = mimeType,
                        _duration = duration
                    )
                )
            }
        }
        return videos
    }

}


class ImageAndVideoLoader(
    context: Context
) : ContentsLoader<GalleryItem>(context) {

    override fun createQueryExecutor(context: Context): QueryExecutor {
        return ImageAndVideoQueryExecutor(context)
    }

    @WorkerThread
    override fun loadContents(fromPos: Int, loadSize: Int): List<GalleryItem> {
        val cur: Cursor = executeQueryIfNotDoneBefore() ?: return ArrayList<GalleryItem>()

        val galleryItems = ArrayList<GalleryItem>()
        cur.moveToPosition(fromPos - 1)
        cur.let {
            for (i in 0 until loadSize) {
                if (!it.moveToNext()) {
                    break
                }
                // find index
                val idIndex = cur.getColumnIndex(MediaStore.Files.FileColumns._ID)
                val filePathIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val mimeTypeIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
                val durationIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.DURATION)
                val mediaTypeIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)

                // get
                val id = cur.getLong(idIndex)
                val filePath = cur.getString(filePathIndex)
                val mimeType = cur.getString(mimeTypeIndex)
                val duration = cur.getLong(durationIndex)
                val mediaType = cur.getInt(mediaTypeIndex)

                if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                    galleryItems.add(
                        Image(
                            _id = id,
                            _filePath = filePath,
                            _mimeType = mimeType
                        )
                    )
                } else if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                    galleryItems.add(
                        Video(
                            _id = id,
                            _filePath = filePath,
                            _mimeType = mimeType,
                            _duration = duration
                        )
                    )
                }
            }
        }
        return galleryItems
    }

}