package com.example.pokedex

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.pokedex.api.MyAPI
import com.example.pokedex.data.UploadResponse
import com.example.pokedex.uploadconfig.UploadRequestBody
import com.example.pokedex.util.getFileName
import com.example.pokedex.util.showSnackbar
import com.example.pokedex.util.snackbar
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class UploadImageMainActivity : AppCompatActivity(), UploadRequestBody.UploadCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var layout: View
    private var ASSESS_STORAGE = 0
    lateinit var selectBtn: Button
    var selectedImageUri: Uri? = null
    lateinit var uploadBtn: Button
    lateinit var progress_bar: ProgressBar
    lateinit var previewImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image_main)
        layout = findViewById(R.id.main_layout)

        selectBtn = findViewById(R.id.chooseImageBtn)
        uploadBtn = findViewById(R.id.uploadImageBtn)
        progress_bar = findViewById(R.id.progress_bar)
        previewImage = findViewById(R.id.previewImage)


        // handle the Choose Image button to trigger
        // the image chooser function
        selectBtn.setOnClickListener {

            showPhonePictures()
        }

        uploadBtn.setOnClickListener {
            uploadImage()
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ASSESS_STORAGE) {
            // Request for Access permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                layout.showSnackbar(R.string.Access_permission_granted, Snackbar.LENGTH_SHORT)
                openImageChooser()
            } else {
                // Permission request was denied.
                layout.showSnackbar(R.string.Access_permission_denied, Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun showPhonePictures() {
        // Check if the permission has been granted
        if (checkSelfPermissionCompat(Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already available, start image selection
            layout.showSnackbar(R.string.permission_available, Snackbar.LENGTH_SHORT)
            openImageChooser()
        } else {
            // Permission is missing and must be requested.
            requestAccessPermission()
        }
    }


    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, UploadImageMainActivity.REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data!!
                    previewImage.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun uploadImage() {
        if (selectedImageUri == null) {
            layout.snackbar("No image selected")

        }
        val parcelFileDescriptor =
            selectedImageUri?.let { contentResolver.openFileDescriptor(it, "r", null) }
                ?: return
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(
            cacheDir,
            contentResolver.getFileName(selectedImageUri!!)
        )
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        progress_bar.progress = 0
        val body = UploadRequestBody(file, "image", this)
        MyAPI().uploadImage(
            MultipartBody.Part.createFormData("image", file.name, body),

            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "image from device")
        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                layout.snackbar(t.message!!)
                //  progress_bar.progress = 0
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    layout.snackbar(it.message.toString())
                    progress_bar.progress = 100
                }
                Toast.makeText(this@UploadImageMainActivity, "Success", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onProgressUpdate(percentage: Int) {
        progress_bar.progress = percentage
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
    }


    private fun requestAccessPermission() {
        // Permission has not been granted and must be requested.
        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            layout.showSnackbar(
                R.string.access_required,
                Snackbar.LENGTH_INDEFINITE, R.string.ok
            ) {
                requestPermissionsCompat(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    ASSESS_STORAGE
                )
            }

        } else {
            layout.showSnackbar(R.string.permission_not_available, Snackbar.LENGTH_SHORT)

            // Request the permission. The result will be received in onRequestPermissionResult().
            requestPermissionsCompat(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                ASSESS_STORAGE
            )
        }
    }


}


