package com.bro.pagingpicker.viewer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bro.pagingpicker.databinding.FragmentVideoViewerBinding
import com.bro.pagingpicker.model.gallery.Video
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by kyunghoon on 2021-01
 */
@AndroidEntryPoint
class VideoViewerFragment : Fragment() {

    companion object {
        const val VIDEO = "video"
    }

    private var video: Video? = null

    private lateinit var binding: FragmentVideoViewerBinding

    private val viewModel: VideoViewerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.image_preview_transition)
//        postponeEnterTransition(750L, TimeUnit.MILLISECONDS)

        arguments?.let {
            video = it.getSerializable(VIDEO) as Video
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
                video = this@VideoViewerFragment.video
                eventListener = viewModel
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        viewModel.onViewCreated(video)
    }

    private fun initViewModel() {
        viewModel.playVideoAction.observe(viewLifecycleOwner, { video ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(video.getFilePath()), video.mimeType)
            startActivity(intent)
        })
    }

}