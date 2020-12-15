package com.bro.pagingpicker.shared.gallery

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import androidx.paging.PositionalDataSource
import com.bro.pagingpicker.model.gallery.Image
import com.bro.pagingpicker.shared.util.closeSafely
import timber.log.Timber
import java.util.*

/**
 * Created by kyunghoon on 2020-12-14
 */
class ImageDataSource(
    private val context: Context,
    private val cursorFactory: CursorFactory
) : PositionalDataSource<Image>() {

    private lateinit var cursor: Cursor

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Image>) {
        Timber.d("### DataSource.loadInitial : %s", Thread.currentThread().name)
        if (cursor == null || cursor.isClosed) {
            Timber.d("### DataSource.loadInitial : %s", cursor?.isClosed)
            cursor = cursorFactory.create()
        }
        val totalCount = computeCount()
        val position = computeInitialLoadPosition(params, totalCount);
        val loadSize = computeInitialLoadSize(params, position, totalCount);
        callback.onResult(loadImage(position, loadSize), position, totalCount);
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Image>) {
        Timber.d("### DataSource.loadRange : %s, startPosition - %d", Thread.currentThread().name, params.startPosition)
        callback.onResult(loadImage(params.startPosition, params.loadSize))
    }

    private fun computeCount(): Int {
        return cursor.count
    }

    private fun loadImage(position: Int, loadSize: Int): List<Image> {
        val cursor = context.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.MIME_TYPE
            ),
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
            null,
            String.format("%s %s", MediaStore.Images.Media.DATE_ADDED, "desc")
        )

        val images = ArrayList<Image>()
        cursor?.moveToPosition(position)
        cursor?.let {
            for (i in 0 until loadSize) {
                if (!it.moveToNext()) {
                    break
                }
                val dataIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val data = cursor.getString(dataIndex);
                images.add(Image(data))
            }
        }
        return images
    }

    override fun invalidate() {
        super.invalidate()
        cursor?.closeSafely()
    }

}