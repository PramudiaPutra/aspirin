package org.d3ifcool.aspirin.ui.home.sosialmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.FileProvider
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.controls.PictureFormat
import org.d3ifcool.aspirin.databinding.ActivityPostingStoryBinding
import org.d3ifcool.aspirin.ui.camera.PhotoPreviewActivity
import java.io.File

class PostingStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostingStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostingStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = PhotoPreviewActivity.pictureResult ?: run {
            finish()
            return
        }

        try {
            result.toBitmap(1000, 1000) { bitmap -> binding.imgAddPosting.setImageBitmap(bitmap) }
        } catch (e: Exception) {
            Toast.makeText(this, "preview error $e", Toast.LENGTH_LONG).show()
        }
    }

    private fun postPhoto() {
        val extension = when (PhotoPreviewActivity.pictureResult!!.format) {
            PictureFormat.JPEG -> "jpg"
            else -> throw RuntimeException("wrong format.")
        }
        val file = File(filesDir, "picture.$extension")
        CameraUtils.writeToFile(PhotoPreviewActivity.pictureResult!!.data, file) {
            if (it != null) {
                val context = this@PostingStoryActivity
                val uri =
                    FileProvider.getUriForFile(context, context.packageName + ".provider", file)
            } else {
                Toast.makeText(
                    this@PostingStoryActivity,
                    "Error while writing file.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}