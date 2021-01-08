package com.bro.pagingpicker.model.gallery

/**
 * Created by kyunghoon on 2020-12-14
 */
data class Video constructor(
    private val _id: Long,
    private val _filePath: String,
    private val _mimeType: String,
    private val _duration: Long
): GalleryItem {
    val mimeType: String get() = _mimeType
    val duration: Long get() = _duration

    override fun getId(): Long {
        return _id
    }

    override fun getFilePath(): String {
        return _filePath
    }

    override fun getType(): GalleryType {
        return GalleryType.VIDEO
    }

}