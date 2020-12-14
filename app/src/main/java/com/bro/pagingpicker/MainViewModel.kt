package com.bro.pagingpicker

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch

/**
 * Created by kyunghoon on 2020-12-14
 */
class MainViewModel @ViewModelInject constructor() : ViewModel() {

    fun onSwipeRefresh() {
        viewModelScope.launch {
            // reload!
        }
    }

    private val swipeRefreshResult = MutableLiveData<Result<Boolean>>()
    val swipeRefreshing: LiveData<Boolean> = swipeRefreshResult.map {
        // Whenever refresh finishes, stop the indicator, whatever the result
        false
    }

    val isEmpty: LiveData<Boolean> = MutableLiveData()

    val isLoading: LiveData<Boolean> = MutableLiveData()
    // TODO
    //    loadSessionsResult.map {
    //        it == Result.Loading
    //    }


}