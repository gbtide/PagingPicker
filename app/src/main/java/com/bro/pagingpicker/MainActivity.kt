package com.bro.pagingpicker

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bro.pagingpicker.databinding.ActivityMainBinding
import com.bro.pagingpicker.util.PermissionUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_READ_WRITE_PERMISSION_CODE = 1001
        private const val FRAGMENT_READ_WRITE_PERMISSION_RATIONALE =
            "read_write_permission_rationale"
    }

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        checkPermission { init() }
    }

    private fun checkPermission(whenSuccess: () -> Unit) {
        if (Build.VERSION.SDK_INT > 22) {
            if (PermissionUtils.hasReadWritePermissions(this)) {
                whenSuccess()
                return
            }

            // memo. 한번 이상 거절 했을 때 + 다시 안보기 체크 안했을 때
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {
                ReadPermissionRationaleFragment().show(
                    supportFragmentManager,
                    FRAGMENT_READ_WRITE_PERMISSION_RATIONALE
                )
                return
            }

            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_READ_WRITE_PERMISSION_CODE
            )

        } else {
            whenSuccess()
        }
    }

    private fun init() {
        initViewModel()
    }

    private fun initViewModel() {
        // do observing if needed
        //
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
                    viewModel.onSuccessGetPermission()
                    init()
                } else {
                    ReadPermissionRationaleFragment().show(
                        supportFragmentManager,
                        FRAGMENT_READ_WRITE_PERMISSION_RATIONALE
                    )
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
                .setNegativeButton(android.R.string.cancel, null)
                .create()
        }
    }

}