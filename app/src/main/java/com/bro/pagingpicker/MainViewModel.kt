package com.bro.pagingpicker

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.bro.pagingpicker.core.domain.LoadPagedPhotoListUseCase
import com.bro.pagingpicker.core.result.Result
import com.bro.pagingpicker.core.result.ResultDataState
import com.bro.pagingpicker.core.util.combine
import com.bro.pagingpicker.model.gallery.Image
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by kyunghoon on 2020-12-14
 */
class MainViewModel @ViewModelInject constructor(
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
    }

}