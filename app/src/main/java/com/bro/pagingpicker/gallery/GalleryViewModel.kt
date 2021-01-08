package com.bro.pagingpicker.gallery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.bro.pagingpicker.core.domain.LoadPagedGalleryListUseCase
import com.bro.pagingpicker.core.domain.LoadPagedPhotoListUseCase
import com.bro.pagingpicker.core.result.Result
import com.bro.pagingpicker.core.result.ResultDataState
import com.bro.pagingpicker.core.util.combine
import com.bro.pagingpicker.model.gallery.GalleryItem
import com.bro.pagingpicker.model.gallery.Image
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by kyunghoon on 2021-01-02
 */
class GalleryViewModel @ViewModelInject constructor(
    private val loadPagedGalleryListUseCase: LoadPagedGalleryListUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    val mainUiData = MutableLiveData<LiveData<PagedList<GalleryItem>>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
    get() = _isLoading

    private val dispatchSwipe = MutableLiveData<Boolean>()

    val swipeRefreshing = dispatchSwipe.combine(_isLoading) { bySwipe, isLoading ->
        bySwipe && isLoading
    }

    private val existContents = loadPagedGalleryListUseCase.dataState.map {
        _isLoading.value = false
        dispatchSwipe.value = false
        it == ResultDataState.Exist
    }

    val isEmpty = existContents.map {
        !it
    }

    init {
        _isLoading.value = true
        viewModelScope.launch {
            // memo. 실제로 데이터 로딩이 끝난 상태에서 Success 가 넘어오는게 아니고,
            // dataSource 를 품고 있는 PagedList 가 build 되서 넘어오기 때문에
            // 실제 loading empty 처리는 별개로 이루어져야 한다.
            loadPagedGalleryListUseCase(Unit).collect {
                when (it) {
                    is Result.Success -> {
                        // main data 복잡해지면 여기서 생성
                        mainUiData.value = it.data
                    }
                    else -> {
                    }
                }
            }
        }
    }

    fun onSwipeRefresh() {
        _isLoading.value = true
        dispatchSwipe.value = true
        refreshList()
    }

    private fun refreshList() {
        // 기존 dataSource invalidate.
        // dataSource invalidate 되면 dataSource 다시 만들고 데이터 다시 불러오기 때문에 별도로 load 할 필요 없다.
        mainUiData.value?.value?.dataSource?.invalidate()
    }

}