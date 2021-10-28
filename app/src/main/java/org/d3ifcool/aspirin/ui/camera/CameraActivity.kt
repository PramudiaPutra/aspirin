package org.d3ifcool.aspirin.ui.camera

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import org.d3ifcool.aspirin.databinding.ActivityCameraBinding
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.camera.setLifecycleOwner(this)

        binding.camera.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)

                val timeStamp: String =
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(
                        Date()
                    )
                PhotoPreviewActivity.pictureResult = result
                val intent = Intent(this@CameraActivity, PhotoPreviewActivity::class.java)
                intent.putExtra("PREVIEW_PHOTO", timeStamp)
                startActivity(intent)
            }
        })

        binding.takePictureButton.setOnClickListener { capturePhotos() }
    }

    private fun capturePhotos() {
        binding.camera.takePictureSnapshot()
    }
}