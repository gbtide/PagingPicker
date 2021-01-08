package com.bro.pagingpicker.model.gallery

/**
 * Created by kyunghoon on 2021-01-08
 */
interface GalleryItem {
    fun getType(): GalleryType
    fun getId(): Long
    fun getFilePath(): String
}