package com.bro.pagingpicker.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.bro.pagingpicker.MainViewModel
import com.bro.pagingpicker.R
import com.bro.pagingpicker.core.util.convertTo
import com.bro.pagingpicker.databinding.FragmentGalleryBinding
import com.bro.pagingpicker.util.PermissionUtils
import com.bro.pagingpicker.viewer.ImageViewerFragment
import com.bro.pagingpicker.viewer.VideoViewerFragment
import dagger.hilt.android.AndroidEntryPoint


private const val ARG_GALLERY_CONTENTS_TYPE = "GALLERY_CONTENTS_TYPE"

@AndroidEntryPoint
class GalleryFragment : Fragment() {
    private var galleryContentsType: GalleryContentsType? = null

    private lateinit var binding: FragmentGalleryBinding

    private val viewModel: GalleryViewModel by viewModels()
    private val mainActivityViewModel: MainViewModel by activityViewModels()

    private lateinit var galleryAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.image_preview_transition)
//        postponeEnterTransition(500L, TimeUnit.MILLISECONDS)

        arguments?.let {
            galleryContentsType = it.getSerializable(ARG_GALLERY_CONTENTS_TYPE).convertTo(
                GalleryContentsType::class.java,
                GalleryContentsType.GALLERY
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
            }
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivityViewModel.successGetPermissionEvent.observe(viewLifecycleOwner, {
            init()
        })

        if (context != null && PermissionUtils.hasReadWritePermissions(requireContext())) {
            // viewModel lazy 로딩이라 permission 받고 viewModel 참조에 접근
            init()
        }
    }

    private fun init() {
        initViewModel()

        galleryAdapter = GalleryAdapter(viewModel)

        binding.recyclerviewGallery.apply {
            adapter = galleryAdapter

            // getItemAnimator 의 초기값은 DefaultItemAnimator
            // memo. run vs apply
            (itemAnimator as DefaultItemAnimator).run {
                supportsChangeAnimations = false
                addDuration = 160L
                moveDuration = 160L
                changeDuration = 160L
                removeDuration = 120L
            }

            layoutManager = GridLayoutManager(context, GalleryAdapter.COLUMN_COUNT)
        }
    }

    private fun initViewModel() {
        // 고민. 이중 옵져빙 구조
        // pull to refresh 하면 dataSource 바뀌는 구조.
        // ComputableLiveData compute 시점과 observing 시점 차이가 나서 이중으로 setting.
        // 참고로, pagedListLiveData 는 dataSource invalidate 되도 유지된다.
        viewModel.mainUiData.observe(viewLifecycleOwner, { pagedListLiveData ->
            pagedListLiveData.observe(viewLifecycleOwner, { galleryItem ->
                galleryAdapter.submitList(galleryItem)
            })
        })

        viewModel.goToImageViewerAction.observe(viewLifecycleOwner, { param ->
//            val preview = param.first.findViewById<View>(R.id.preview_image);
//            val extras = FragmentNavigatorExtras(preview to preview.transitionName)
            val bundle = bundleOf(ImageViewerFragment.IMAGE to param.second)
            findNavController().navigate(R.id.to_image_viewer, bundle)
        })

        viewModel.goToVideoViewerAction.observe(viewLifecycleOwner, { param ->
//            val preview = param.first.findViewById<View>(R.id.preview_image);
//            val extras = FragmentNavigatorExtras(preview to preview.transitionName)
            val bundle = bundleOf(VideoViewerFragment.VIDEO to param.second)
            findNavController().navigate(R.id.to_video_viewer, bundle)
        })

        binding.viewModel = viewModel

    }

}