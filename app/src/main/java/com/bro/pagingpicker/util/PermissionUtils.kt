package com.bro.pagingpicker.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Created by kyunghoon on 2021-01-02
 */
object PermissionUtils {

    fun hasReadWritePermissions(context : Context): Boolean {
        return if (Build.VERSION.SDK_INT > 22) {
            (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)
        } else {
            true
        }
    }

}