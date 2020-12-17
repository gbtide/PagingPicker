package com.bro.pagingpicker.shared.gallery

import androidx.paging.PositionalDataSource
import com.bro.pagingpicker.model.gallery.Image
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by kyunghoon on 2020-12-14
 */
class ImageDataSource @Inject constructor(
    private val loader: ImageLoader
) : PositionalDataSource<Image>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Image>) {
        Timber.d("### DataSource.loadInitial : %s", Thread.currentThread().name)
        val totalCount = loader.computeCount()
        val position = computeInitialLoadPosition(params, totalCount);
        val loadSize = computeInitialLoadSize(params, position, totalCount);
        callback.onResult(loader.loadImage(position, loadSize), position, totalCount);
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Image>) {
        Timber.d(
            "### DataSource.loadRange : %s, startPosition - %d",
            Thread.currentThread().name,
            params.startPosition
        )
        callback.onResult(loader.loadImage(params.startPosition, params.loadSize))
    }

    override fun invalidate() {
        super.invalidate()
        loader.reset()
    }

}