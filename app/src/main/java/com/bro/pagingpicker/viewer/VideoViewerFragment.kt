package com.bro.pagingpicker.viewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.bro.pagingpicker.R
import com.bro.pagingpicker.databinding.FragmentVideoViewerBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

/**
 * Created by kyunghoon on 2021-01
 */
@AndroidEntryPoint
class VideoViewerFragment : Fragment() {

    companion object {
        const val URI = "uri"
    }

    private var fileUri: String? = null

    private lateinit var binding: FragmentVideoViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.image_preview_transition)
//        postponeEnterTransition(750L, TimeUnit.MILLISECONDS)

        arguments?.let {
            fileUri = it.getString(URI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVideoViewerBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                uri = fileUri
            }

        fileUri?.let {
            Glide.with(this)
                .load(fileUri)
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transition(DrawableTransitionOptions.withCrossFade(100))
                .into(binding.videoViewerView)
        }

        return binding.root
    }
}