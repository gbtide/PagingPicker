package com.bro.pagingpicker

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bro.pagingpicker.core.util.SingleLiveEvent

/**
 * Created by kyunghoon on 2020-12-14
 */
class MainViewModel @ViewModelInject constructor(
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _successGetPermissionEvent = SingleLiveEvent<Unit>()
    val successGetPermissionEvent: LiveData<Unit>
        get() = _successGetPermissionEvent

    fun onSuccessGetPermission() {
        _successGetPermissionEvent.call()
    }

}