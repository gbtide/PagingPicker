package com.bro.pagingpicker.core.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.bro.pagingpicker.core.di.IoDispatcher
import com.bro.pagingpicker.core.domain.base.FlowUseCase
import com.bro.pagingpicker.core.gallery.ImageAndVideoDataSourceFactory
import com.bro.pagingpicker.core.result.Result
import com.bro.pagingpicker.core.result.ResultDataState
import com.bro.pagingpicker.model.gallery.GalleryItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by kyunghoon on 2021-01-02
 */
class LoadPagedGalleryListUseCase @Inject constructor(
    dataSourceFactory: ImageAndVideoDataSourceFactory,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, LiveData<PagedList<GalleryItem>>>(dispatcher) {

    companion object {
        private const val PAGE_SIZE = 30
    }

    private val livePagedListBuilder: LivePagedListBuilder<Int, GalleryItem>

    private val _dataState = MutableLiveData<ResultDataState>()
    val dataState: LiveData<ResultDataState>
        get() = _dataState

    init {
        livePagedListBuilder = LivePagedListBuilder(dataSourceFactory, PAGE_SIZE).apply {
            setBoundaryCallback(object : PagedList.BoundaryCallback<GalleryItem>() {
                override fun onZeroItemsLoaded() {
                    super.onZeroItemsLoaded()
                    Timber.d("### onZeroItemsLoaded : ")
                    _dataState.value = ResultDataState.Empty
                }

                override fun onItemAtFrontLoaded(itemAtFront: GalleryItem) {
                    super.onItemAtFrontLoaded(itemAtFront)
                    Timber.d("### onItemAtFrontLoaded : %s", itemAtFront)
                    _dataState.value = ResultDataState.Exist
                }

                override fun onItemAtEndLoaded(itemAtEnd: GalleryItem) {
                    super.onItemAtEndLoaded(itemAtEnd)
                    Timber.d("### onItemAtEndLoaded : %s", itemAtEnd)
                }
            })
        }
    }

    override fun execute(parameters: Unit): Flow<Result<LiveData<PagedList<GalleryItem>>>> {
        return flow {
            try {
                emit(Result.Success(livePagedListBuilder.build()))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
    }

}