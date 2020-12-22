package com.bro.pagingpicker.shared.domain

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.bro.pagingpicker.model.gallery.Image
import com.bro.pagingpicker.shared.di.IoDispatcher
import com.bro.pagingpicker.shared.gallery.ImageDataSourceFactory
import com.bro.pagingpicker.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by kyunghoon on 2020-12-16
 */
class LoadPagedPhotoListUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val imageDataSourceFactory: ImageDataSourceFactory
) : FlowUseCase<Unit, LiveData<PagedList<Image>>>(dispatcher) {

    companion object {
        private const val PAGE_SIZE = 30
    }

    override fun execute(parameters: Unit): Flow<Result<LiveData<PagedList<Image>>>> {
        return flow {
            emit(Result.Loading)

            try {
                emit(Result.Success(getPagedListLiveData()))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
    }

    private fun getPagedListLiveData(): LiveData<PagedList<Image>> {
        return LivePagedListBuilder(
            imageDataSourceFactory,
            PAGE_SIZE
        ).build()
    }

}