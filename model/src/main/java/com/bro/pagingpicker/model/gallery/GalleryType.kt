package com.bro.pagingpicker.model.gallery

/**
 * Created by kyunghoon on 2021-01-08
 */
enum class GalleryType(
    val value: Int
) {
    IMAGE(0),
    VIDEO(1);

    companion object {
        fun find(value: Int): GalleryType? {
            return values().firstOrNull { it.value == value }
        }
    }
}