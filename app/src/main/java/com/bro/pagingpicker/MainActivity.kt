package com.bro.pagingpicker

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.bro.pagingpicker.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

// hilt 사용하면 반드시 습관처럼 해줘야함
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_READ_WRITE_PERMISSION_CODE = 1001
        private const val FRAGMENT_READ_WRITE_PERMISSION_RATIONALE = "read_write_permission_rationale"
    }

    private lateinit var binding: ActivityMainBinding

    // memo. ViewModelLazy 로 넘어옴
    private val viewModel: MainViewModel by viewModels()

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // memo. 종종 하는 실수. 아래 세팅 넣어야 xml 바인딩 됨. xml에 묶여있는 Transformations 등 liveData 오동작 막기
        binding.lifecycleOwner = this

        checkPermission { init() }
    }

    private fun checkPermission(whenSuccess: () -> Unit) {
        if (Build.VERSION.SDK_INT > 22) {
            var granted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            if (granted) {
                whenSuccess
            }

            // memo. 한번 이상 거절 했을 때 + 다시 안보기 체크 안했을 때
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ReadPermissionRationaleFragment()
                    .show(supportFragmentManager, FRAGMENT_READ_WRITE_PERMISSION_RATIONALE)
                return
            }

            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_READ_WRITE_PERMISSION_CODE)
        } else {
            whenSuccess()
        }
    }

    private fun init() {
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

            layoutManager = GridLayoutManager(context, MainAdapter.COLUMN_COUNT)
        }
    }

    private fun initViewModel() {
        viewModel.mainUiData.observe(this, { pagedListLiveData ->
            pagedListLiveData.observe(this, { images ->
                mainAdapter.submitList(images)
            })
        })

        viewModel.loadStatusObserver.observe(this, {

        })

        binding.viewModel = viewModel
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_READ_WRITE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init()
                } else {
                    ReadPermissionRationaleFragment()
                        .show(supportFragmentManager, FRAGMENT_READ_WRITE_PERMISSION_RATIONALE)
                    finish()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    class ReadPermissionRationaleFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return MaterialAlertDialogBuilder(context)
                .setMessage(R.string.read_write_permission_rationale)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_WRITE_PERMISSION_CODE
                    )
                }
                .setNegativeButton(android.R.string.cancel, null) // Give up
                .create()
        }
    }

}