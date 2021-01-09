package com.bro.pagingpicker.viewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bro.pagingpicker.R
import com.bro.pagingpicker.databinding.FragmentImageViewerBinding
import com.davemorrissey.labs.subscaleview.ImageSource


/**
 * A simple [Fragment] subclass.
 * Use the [ImageViewerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageViewerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var uri: String? = null

    private lateinit var binding: FragmentImageViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uri = it.getString(URI)
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
            }

        if (uri != null) {
            binding.imageViewerView.setImage(ImageSource.uri(uri!!))
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ImageViewerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImageViewerFragment().apply {
                arguments = Bundle().apply {
                    putString(URI, param1)
                }
            }

        const val URI = "uri"
    }
}