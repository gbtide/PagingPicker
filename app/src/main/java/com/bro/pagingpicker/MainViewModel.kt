package com.bro.pagingpicker

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.bro.pagingpicker.model.gallery.Image
import com.bro.pagingpicker.shared.domain.LoadPagedPhotoListUseCase
import com.bro.pagingpicker.shared.result.Result
import com.bro.pagingpicker.shared.result.data
import com.bro.pagingpicker.shared.result.succeeded
import com.bro.pagingpicker.shared.util.checkAllMatched
import com.bro.pagingpicker.shared.util.combine
import com.bro.pagingpicker.shared.util.map
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by kyunghoon on 2020-12-14
 */
class MainViewModel @ViewModelInject constructor(
    private val loadPagedPhotoListUseCase: LoadPagedPhotoListUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val loadResult = MutableLiveData<Result<LiveData<PagedList<Image>>>>()

    val loadStatusObserver = MediatorLiveData<Unit>().apply {
        addSource(loadResult) {
            when (it) {
                is Result.Success -> {
                    dispatchSwipe.value = false
                    mainUiData.value = it.data
                }
                is Result.Error -> {
                }
                is Result.Loading -> {
                }
            }.checkAllMatched
        }
    }

    val mainUiData = MutableLiveData<LiveData<PagedList<Image>>>()

    val isLoading = loadResult.map { it == Result.Loading }

    private val dispatchSwipe = MutableLiveData<Boolean>()

    val swipeRefreshing = dispatchSwipe.combine(loadResult) { bySwipe, result ->
        bySwipe && (result == Result.Loading)
    }

    val isEmpty = loadResult.map {
        if (it.succeeded) {
            val response = it.data!!.value
            response?.size == 0
        } else {
            false
        }
    }

    init {
        loadList()
    }

    fun onSwipeRefresh() {
        dispatchSwipe.value = true
        loadList()
    }

    private fun loadList() {
        viewModelScope.launch {
            loadPagedPhotoListUseCase(Unit)
                .collect {
                    loadResult.value = it
                }
        }
    }

}