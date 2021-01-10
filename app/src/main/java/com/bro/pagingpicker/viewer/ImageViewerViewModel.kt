package com.bro.pagingpicker.viewer

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.bro.pagingpicker.model.gallery.Image

/**
 * Created by kyunghoon on 2021-01-10
 */
class ImageViewerViewModel @ViewModelInject constructor(
) : ViewModel() {

    companion object {
        private const val TAG = "ImageViewerViewModel"
    }

    private var image: Image? = null

    fun onViewCreated(image: Image?) {
        this.image = image
    }

}