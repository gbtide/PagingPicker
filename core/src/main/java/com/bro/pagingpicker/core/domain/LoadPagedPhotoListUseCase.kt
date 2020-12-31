package com.bro.pagingpicker.core.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.bro.pagingpicker.core.di.IoDispatcher
import com.bro.pagingpicker.core.domain.base.FlowUseCase
import com.bro.pagingpicker.core.gallery.ImageDataSourceFactory
import com.bro.pagingpicker.core.result.Result
import com.bro.pagingpicker.core.result.ResultDataState
import com.bro.pagingpicker.model.gallery.Image
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import kotlin.Exception

/**
 * Created by kyunghoon on 2020-12-16
 */
class LoadPagedPhotoListUseCase @Inject constructor(
        imageDataSourceFactory: ImageDataSourceFactory,
        @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, LiveData<PagedList<Image>>>(dispatcher) {

    companion object {
        private const val PAGE_SIZE = 30
    }

    private val livePagedListBuilder: LivePagedListBuilder<Int, Image>

    private val _dataState = MutableLiveData<ResultDataState>()
    val dataState: LiveData<ResultDataState>
        get() = _dataState
    //
    // memo. flow 가 적절하지 않은 것 같다. success 던지기. & builder 열어주기? .. reactive 하지 않은
    //

    init {
        livePagedListBuilder = LivePagedListBuilder(imageDataSourceFactory, PAGE_SIZE).apply {
            setBoundaryCallback(object : PagedList.BoundaryCallback<Image>() {
                override fun onZeroItemsLoaded() {
                    super.onZeroItemsLoaded()
                    Timber.d("### onZeroItemsLoaded : ")
                    _dataState.value = ResultDataState.Empty
                }

                override fun onItemAtFrontLoaded(itemAtFront: Image) {
                    super.onItemAtFrontLoaded(itemAtFront)
                    Timber.d("### onItemAtFrontLoaded : %s", itemAtFront)
                    _dataState.value = ResultDataState.Exist
                }

                override fun onItemAtEndLoaded(itemAtEnd: Image) {
                    super.onItemAtEndLoaded(itemAtEnd)
                    Timber.d("### onItemAtEndLoaded : %s", itemAtEnd)
                }
            })
        }
    }

    override fun execute(parameters: Unit): Flow<Result<LiveData<PagedList<Image>>>> {
        return flow {
            try {
                emit(Result.Success(livePagedListBuilder.build()))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
    }

}