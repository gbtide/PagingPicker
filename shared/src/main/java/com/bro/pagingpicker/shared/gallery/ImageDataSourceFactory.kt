package com.bro.pagingpicker.shared.gallery

import androidx.paging.DataSource
import com.bro.pagingpicker.model.gallery.Image
import javax.inject.Inject

/**
 * Created by kyunghoon on 2020-12-18
 */
class ImageDataSourceFactory @Inject constructor(private val imageDataSource: ImageDataSource): DataSource.Factory<Int, Image>() {
    override fun create(): DataSource<Int, Image> {
        return imageDataSource
    }
}