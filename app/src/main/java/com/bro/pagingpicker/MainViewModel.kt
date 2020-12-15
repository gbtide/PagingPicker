package com.bro.pagingpicker

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.bro.pagingpicker.model.gallery.Image
import com.bro.pagingpicker.shared.domain.LoadPhotoUseCase
import com.bro.pagingpicker.shared.domain.LoadPhotoUseCaseResult
import com.bro.pagingpicker.shared.result.Result
import com.bro.pagingpicker.shared.result.data
import com.bro.pagingpicker.shared.util.checkAllMatched
import com.bro.pagingpicker.shared.util.map
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by kyunghoon on 2020-12-14
 */
class MainViewModel @ViewModelInject constructor(
    private val loadPhotoUseCase: LoadPhotoUseCase
) : ViewModel() {

    private val loadPhotoResult = MutableLiveData<Result<LoadPhotoUseCaseResult>>()

    private var photos: LiveData<List<Image>> = loadPhotoResult.map {
        // 아래 표현 복습 * 10
        it.data?.list ?: ArrayList()
    }

    // val vs var
    val mainUiData = photos.map {
        // TODO 필요한 데이터로 변경
        it
    }

    private var swipeRequest = false
    val swipeRefreshing: LiveData<Boolean> = loadPhotoResult.map {
        // Whenever refresh finishes, stop the indicator, whatever the result
        if (swipeRequest) {
            when (it) {
                is Result.Success -> {
                    swipeRequest = false
                    false
                }
                is Result.Error -> {
                    swipeRequest = false
                    false
                }
                is Result.Loading -> {
                    true
                }
            }.checkAllMatched
        }
        false
    }

    fun onCreateView() {
        load()
    }

    fun onSwipeRefresh() {
        swipeRequest = true
        load()
    }

    private fun load() {
        viewModelScope.launch {
            loadPhotoUseCase(Unit).collect {
                loadPhotoResult.value = it
            }
        }
    }

    val isEmpty: MutableLiveData<Boolean> = MutableLiveData()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    // TODO
    //    loadSessionsResult.map {
    //        it == Result.Loading
    //    }


}