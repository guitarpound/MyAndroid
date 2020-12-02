package com.codemobiles.myandroid

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.codemobiles.myandroid.databinding.ActivityFormBinding
import com.codemobiles.myandroid.databinding.ActivityMainBinding
import com.codemobiles.myandroid.network.NetworkAPI
import com.codemobiles.myandroid.network.NetworkService
import com.codemobiles.myandroid.utilities.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import pl.aprilapps.easyphotopicker.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class FormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding
    private var permissionGranted = false
    private var easyImage: EasyImage? = null
    private var file: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        easyImage = EasyImage.Builder(this)
            .setChooserTitle("Pick media")
            .setCopyImagesToPublicGalleryFolder(false)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .setFolderName("EasyImage sample")
            .allowMultiple(true)
            .build()

        setupEventWidget()

        checkRuntimePermission()
        setupToolbar()
    }



    private fun setupEventWidget() {
        binding.camera.setOnClickListener() {
            if (permissionGranted) {
                easyImage?.openCameraForImage(this)
            }
        }

        binding.gallery.setOnClickListener() {
            if (permissionGranted) {
                easyImage?.openGallery(this)
            }
        }

        binding.productSubmit.setOnClickListener() {
            sendToServer()
        }
    }

    private fun sendToServer() {

        var name = binding.productEdittextName.text.toString()
        var price = binding.productEdittextPrice.text.toString()
        var stock = binding.productEdittextStock.text.toString()

        // Sent Message
        val bodyText = HashMap<String, RequestBody>().apply {
            val mediaType = MediaType.parse(MEDIA_TYPE_TEXT)
            this[API_PRODUCT_FORM_NAME] =
                RequestBody.create(mediaType, if (name.isEmpty()) "-" else name)
            this[API_PRODUCT_FORM_PRICE] =
                RequestBody.create(mediaType, if (price.isEmpty()) "0" else price)
            this[API_PRODUCT_FORM_STOCK] =
                RequestBody.create(mediaType, if (stock.isEmpty()) "0" else stock)
        }

        // Send Image
        val bodyImage: MultipartBody.Part? = covertByteArray(file!!).let {
            byteArray ->

            val mediaType = MediaType.parse(MEDIA_TYPE_IMAGE)
            val reqFile = RequestBody.create(mediaType, byteArray)
            MultipartBody.Part.createFormData(API_PRODUCT_FORM_PHOTO, file!!.name, reqFile)
        }



        val call = NetworkService.getClient().create(NetworkAPI::class.java).addProduct(bodyText, bodyImage)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    finish()
                }else{
                    Toast.makeText(applicationContext, "network fail", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT).show()
            }


        })
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setOnClickListener {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun checkRuntimePermission() {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                        if (report.areAllPermissionsGranted()) {
                            // open camera and Gallery
                            //easyImage?.openGallery(this@FormActivity)
                            permissionGranted = true
                        } else {
                            finish()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) { /* ... */
                        token!!.continuePermissionRequest()
                    }
                }).check()
        }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        // parameter
        easyImage!!.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {
                override fun onMediaFilesPicked(
                    imageFiles: Array<MediaFile>,
                    source: MediaSource
                ) {
                    onPhotosReturned(imageFiles)
                }

                override fun onImagePickerError(
                    error: Throwable,
                    source: MediaSource
                ) {
                    //Some error handling
                    error.printStackTrace()
                }

                override fun onCanceled(source: MediaSource) {
                    //Not necessary to remove any files manually anymore
                }
            })
    }

    private fun onPhotosReturned(returnedPhotos: Array<MediaFile>) {
        val imagesFiles: List<MediaFile> =
            ArrayList(listOf(*returnedPhotos))
        file = imagesFiles[0].file

        Glide.with(applicationContext).load(file).into(binding.productImageview)
        binding.productImageview.visibility = View.VISIBLE

        binding.photoLayout.gravity = Gravity.END
        binding.photoLayout.setPadding(0, 12, 12, 0)
    }

    private fun covertByteArray(file: File): ByteArray {
        val size = file.length().toInt()
        val bytes = ByteArray(size)
        try {
            val buf =
                BufferedInputStream(FileInputStream(file))
            buf.read(bytes, 0, bytes.size)
            buf.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bytes
    }
}

// Glide คือ ดาวน์โหลด images จาก network