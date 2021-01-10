package com.bro.pagingpicker.viewer

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bro.pagingpicker.core.util.SingleLiveEvent
import com.bro.pagingpicker.model.gallery.Video

/**
 * Created by kyunghoon on 2021-01-10
 */
class VideoViewerViewModel @ViewModelInject constructor(
) : ViewModel(), VideoViewerEventListener {

    companion object {
        private const val TAG = "VideoViewerViewModel"
    }

    private var video: Video? = null

    private val _playVideoAction = SingleLiveEvent<Video>()
    val playVideoAction: LiveData<Video>
        get() = _playVideoAction

    fun onViewCreated(video: Video?) {
        this.video = video
    }

    override fun onClickPreviewImage(video: Video) {
        _playVideoAction.value = video
    }
}

interface VideoViewerEventListener {
    fun onClickPreviewImage(video: Video)
}