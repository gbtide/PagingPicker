package com.bro.pagingpicker.viewer

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bro.pagingpicker.core.util.SingleLiveEvent

/**
 * Created by user on 2021-01-10
 */
class ImageViewerViewModel @ViewModelInject constructor(
) : ViewModel() {

    companion object {
        private const val TAG = "ImageViewerViewModel"
    }

    private val _playVideoAction = SingleLiveEvent<String>()
    val playVideoAction: LiveData<String>
        get() = _playVideoAction


}