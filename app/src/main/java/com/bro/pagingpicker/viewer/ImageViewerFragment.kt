package com.bro.pagingpicker.viewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bro.pagingpicker.databinding.FragmentImageViewerBinding
import com.bro.pagingpicker.gallery.GalleryViewModel
import com.bro.pagingpicker.model.gallery.Image
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by kyunghoon on 2021-01
 */
@AndroidEntryPoint
class ImageViewerFragment : Fragment() {

    companion object {
        const val IMAGE = "image"
    }

    private var image: Image? = null

    private lateinit var binding: FragmentImageViewerBinding

    private val viewModel: ImageViewerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.image_preview_transition)
//        postponeEnterTransition(750L, TimeUnit.MILLISECONDS)

        arguments?.let {
            image = it.getSerializable(IMAGE) as Image
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImageViewerBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                image = this@ImageViewerFragment.image
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        viewModel.onViewCreated(image)
    }

    private fun initViewModel() {
    }

}