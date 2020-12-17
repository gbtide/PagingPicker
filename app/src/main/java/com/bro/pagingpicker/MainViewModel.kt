package com.bro.pagingpicker

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.bro.pagingpicker.model.gallery.Image
import com.bro.pagingpicker.shared.domain.LoadPhotoUseCase
import com.bro.pagingpicker.shared.domain.LoadPhotoUseCaseResult
import com.bro.pagingpicker.shared.result.Result
import com.bro.pagingpicker.shared.result.data
import com.bro.pagingpicker.shared.result.succeeded
import com.bro.pagingpicker.shared.util.combine
import com.bro.pagingpicker.shared.util.map
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by kyunghoon on 2020-12-14
 */
class MainViewModel @ViewModelInject constructor(
    private val loadPhotoUseCase: LoadPhotoUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val loadResult = MutableLiveData<Result<LoadPhotoUseCaseResult>>()

    private val photos: LiveData<List<Image>> = loadResult.map {
        duringSwipe.value = false

        // 아래 표현 복습 * 10
        it.data?.list ?: ArrayList()
    }

    val mainUiData = photos.map {
        // TODO 필요한 데이터로 변경
        it
    }

    private val duringSwipe = MutableLiveData<Boolean>()

    val swipeRefreshing = duringSwipe.combine(loadResult) { swiping, result ->
        if (swiping) {
            result == Result.Loading
        } else {
            false
        }
    }

    val isEmpty = loadResult.map {
        if (it.succeeded) {
            it.data?.list?.isEmpty() ?: true
        } else {
            false
        }
    }

    val isLoading = loadResult.map {
        it == Result.Loading
    }

    init {
        loadList()
    }

    fun onSwipeRefresh() {
        duringSwipe.value = true
        loadList()
    }

    private fun loadList() {
        viewModelScope.launch {
            loadPhotoUseCase(Unit).collect {
                loadResult.value = it
            }
        }
    }

}