package com.bro.pagingpicker.shared.gallery

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore

/**
 * Created by kyunghoon on 2020-12-14
 */
interface CursorFactory {
    fun create(): Cursor?
}

class ImageCursorFactory(private val context: Context) : CursorFactory {

    override fun create(): Cursor? {
        return context.contentResolver.query(
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
    }

}