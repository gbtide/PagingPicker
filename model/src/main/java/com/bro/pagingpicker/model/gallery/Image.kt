package com.bro.pagingpicker.model.gallery

import java.io.Serializable

/**
 * Created by kyunghoon on 2020-12-14
 */
data class Image constructor(
    private val _id: Long,
    private val _filePath: String,
    private val _mimeType: String,
): GalleryItem, Serializable {
    val mimeType: String get() = _mimeType

    override fun getId(): Long {
        return _id
    }

    override fun getFilePath(): String {
        return _filePath
    }

    override fun getType(): GalleryType {
        return GalleryType.IMAGE
    }
}