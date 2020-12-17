package com.bro.pagingpicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bro.pagingpicker.databinding.ActivityMainBinding
import com.bro.pagingpicker.model.Cons
import com.bro.pagingpicker.model.gallery.Image
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

// hilt 사용하면 반드시 습관처럼 해줘야함
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // memo. ViewModelLazy 로 넘어옴
    private val viewModel: MainViewModel by viewModels()

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // 아래 세팅 없으니 Transformations 안먹힘
        binding.lifecycleOwner = this

        initViewModel()

        mainAdapter = MainAdapter()
        binding.recyclerviewGalleryImage.apply {
            adapter = mainAdapter

            (itemAnimator as DefaultItemAnimator).run {
                supportsChangeAnimations = false
                addDuration = 160L
                moveDuration = 160L
                changeDuration = 160L
                removeDuration = 120L
            }
        }
    }

    private fun initViewModel() {
        viewModel.mainUiData.observe(this, { images ->
            mainAdapter.submitList(images)
        })

        binding.viewModel = viewModel
    }


}