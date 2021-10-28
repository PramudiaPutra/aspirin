package org.d3ifcool.aspirin.ui.camera

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.otaliastudios.cameraview.PictureResult
import org.d3ifcool.aspirin.databinding.ActivityPhotoPreviewBinding
import org.d3ifcool.aspirin.ui.home.sosialmedia.PostingStoryActivity

class PhotoPreviewActivity : AppCompatActivity() {

    companion object {
        var pictureResult: PictureResult? = null
    }

    private lateinit var binding: ActivityPhotoPreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confirmPhotoButton.setOnClickListener {
            startActivity(
                Intent(this, PostingStoryActivity::class.java)
            )
        }

        val result = pictureResult ?: run {
            finish()
            return
        }

        try {
            result.toBitmap(1000, 1000) { bitmap -> binding.photoPreview.setImageBitmap(bitmap) }
        } catch (e: Exception) {
            Toast.makeText(this, "preview error $e", Toast.LENGTH_LONG).show()
        }
    }
}