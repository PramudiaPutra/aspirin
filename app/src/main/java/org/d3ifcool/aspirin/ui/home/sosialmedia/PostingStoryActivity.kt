package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.viewmodel.sosialmedia.PostingViewModel
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseUser
import com.otaliastudios.cameraview.CameraUtils
import org.d3ifcool.aspirin.databinding.ActivityPostingStoryBinding
import org.d3ifcool.aspirin.ui.camera.PhotoPreviewActivity
import java.io.File

class PostingStoryActivity : AppCompatActivity() {

    lateinit var photoUri: Uri
    lateinit var username: String

    private lateinit var binding: ActivityPostingStoryBinding
    private val viewModel: PostingViewModel by lazy {
        ViewModelProvider(this).get(PostingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostingStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnKirimKegiatan.setOnClickListener {
            posting()
            val intent = Intent(this, SosialMediaActivity::class.java)
            startActivity(intent)
        }

        viewModel.authUser.observe(this, {getCurrentUser(it)})

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

    private fun getCurrentUser(user: FirebaseUser?) {
        username = user!!.displayName.toString()
    }

    private fun posting() {
        val sdf = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())

        if (binding.edtJudulKegiatan.text.isEmpty()) {
            showMessage(R.string.judul_kosong)
        }

        if (binding.edtDeskripsiKegiatan.text.isEmpty()) {
            showMessage(R.string.lokasi_kosong)
        }

        if (binding.edtLokasiKegiatan.text.isEmpty()) {
            showMessage(R.string.lokasi_kosong)
        }

        val judul = binding.edtJudulKegiatan.text.toString()
        val deskripsi = binding.edtDeskripsiKegiatan.text.toString()
        val lokasi = binding.edtLokasiKegiatan.text.toString()
        val file = File(filesDir, "${System.currentTimeMillis()}.jpg")
        CameraUtils.writeToFile(PhotoPreviewActivity.pictureResult!!.data, file) {
            if (it != null) {
                val context = this@PostingStoryActivity
                photoUri =
                    FileProvider.getUriForFile(context, context.packageName + ".provider", file)
                viewModel.postData(username, judul, lokasi, deskripsi, currentDate, photoUri)
            } else {
                Toast.makeText(
                    this@PostingStoryActivity,
                    "Error while writing file.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
//        val postingan = PostingData(
//            "Akhdan Pangestuaji",
//            judul,
//            lokasi,
//            deskripsi,
//            currentDate,
//            null
//        )
    }

    private fun showMessage(messageResId: Int) {
        Toast.makeText(applicationContext, messageResId, Toast.LENGTH_LONG).apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }
}