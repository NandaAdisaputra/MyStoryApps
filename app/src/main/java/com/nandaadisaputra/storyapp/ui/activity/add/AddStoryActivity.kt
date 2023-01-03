package com.nandaadisaputra.storyapp.ui.activity.add

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.crocodic.core.extension.isEmptyRequired
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.tos
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.nandaadisaputra.storyapp.R
import com.nandaadisaputra.storyapp.base.activity.BaseActivity
import com.nandaadisaputra.storyapp.data.constant.Const
import com.nandaadisaputra.storyapp.databinding.ActivityAddStoryBinding
import com.nandaadisaputra.storyapp.ui.activity.main.MainActivity
import com.nandaadisaputra.storyapp.ui.utils.createCustomTempFile
import com.nandaadisaputra.storyapp.ui.utils.rotateBitmap
import com.nandaadisaputra.storyapp.ui.utils.showError
import com.nandaadisaputra.storyapp.ui.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class AddStoryActivity :
    BaseActivity<ActivityAddStoryBinding, AddStoryViewModel>(R.layout.activity_add_story) {

    private lateinit var currentPhotoPath: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSIONS -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    tos(Const.PICTURE.PERMISSIONCAMERA)
                }
            }
            LOCATION_REQUEST_CODE_PERMISSIONS -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    tos(Const.PICTURE.LOCATIONPERMISSION)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionBar()
        darkMode()
        supportActionBar?.title = Const.TITLE.ADD
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        if (!allPermissionsGranted()) {
            requestMultiplePermissions.launch(
                REQUIRED_PERMISSIONS
            )
        }

        with(binding) {
            btnCamera.setOnClickListener {
                startTakePhoto()
            }
            btnGallery.setOnClickListener {
                startGallery()
            }
            btnUpload.setOnClickListener {
                showLoading(true)
                if (binding.edtDescription.isEmptyRequired(R.string.label_must_fill)
                ) {
                    showLoading(false)
                    return@setOnClickListener
                }
                val desc = edtDescription.text.toString()
                if (ContextCompat.checkSelfPermission(
                        baseContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        baseContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    uploadImageWithLocation(
                        desc,
                        getFile as File
                    )
                    showLoading(false)

                } else {
                    viewModel.uploadImage(
                        desc,
                        getFile as File,
                        null,
                        null
                    )
                    showLoading(false)
                }
            }
            viewModel.isError.observe(this@AddStoryActivity) {
                if (it) {
                    showError(it, applicationContext, Const.PICTURE.UPLOADFAILED)
                    showLoading(false)
                } else {
                    openActivity<MainActivity> {  }
                    finish()
                    showLoading(false)
                }
            }
        }
    }

    private fun darkMode() {
        viewModel.getTheme.observe(this) { isDarkMode ->
            checkDarkMode(isDarkMode)
        }
    }

    private fun checkDarkMode(isDarkMode: Boolean) {
        when (isDarkMode) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                }
                else -> {
                }
            }
        }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this.applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun uploadImageWithLocation(desc: String, getFile: File?) {
        if (
            checkLocationPermission()
        ) {
//            tos(Const.PICTURE.LOCATIONPERMISSION)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    viewModel.uploadImage(
                        desc,
                        getFile as File,
                        location.longitude,
                        location.latitude
                    )
                } else {
                    tos(Const.PICTURE.LOCATIONNOTFOUND)
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            getFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                true
            )

            binding.ivPhoto.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)

            getFile = myFile

            binding.ivPhoto.setImageURI(selectedImg)
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { _ ->
            }
        }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.nandaadisaputra.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, Const.PICTURE.CHOOSE)
        launcherIntentGallery.launch(chooser)
    }
    private fun setActionBar() {
        supportActionBar?.title = Const.TITLE.ADD
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun showLoading(state: Boolean) {
        binding.addProgressBar.isVisible = state
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
    companion object {
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val LOCATION_REQUEST_CODE_PERMISSIONS = 99
    }
}