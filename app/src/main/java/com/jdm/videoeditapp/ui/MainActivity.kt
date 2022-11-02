package com.jdm.videoeditapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.jdm.videoeditapp.R
import com.jdm.videoeditapp.base.BaseActivity
import com.jdm.videoeditapp.databinding.ActivityMainBinding
import com.jdm.videoeditapp.ui.media.MediaSourceActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_main

    private val storagePermissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    private val requestStoragePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        var permissionFlag = true
        for (entry in it.entries) {
            if (!entry.value) {
                permissionFlag = false
                break
            }
        }
        if (!permissionFlag) {
            Toast.makeText(this, R.string.permissions_storage_not_granted_message, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            goToVideoEditActivity()
        }

    }

    override fun subscribe() {
    }

    override fun initEvent() {
        with(binding) {
            editVideo.setOnClickListener {
                if (isStoragePermissionGranted()) {
                    goToVideoEditActivity()
                } else {
                    requestStoragePermission()
                }
            }
        }
    }

    override fun initView() {

    }
    fun goToVideoEditActivity() {
        Intent(this, MediaSourceActivity::class.java).run { startActivity(this) }
    }
    fun requestStoragePermission() {
        requestStoragePermissionLauncher.launch(storagePermissions)
    }
    fun isStoragePermissionGranted() = storagePermissions.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}