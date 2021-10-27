package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.viewmodel.sosialmedia.PostingData
import org.d3ifcool.aspirin.databinding.ActivityPostingStoryBinding
import java.text.SimpleDateFormat
import java.util.*

class PostingStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostingStoryBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostingStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnKirimKegiatan.setOnClickListener {
            posting()
        }
    }

    private fun posting(){
        val sdf = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())

       if (binding.edtJudulKegiatan.text.isEmpty()){
           showMessage(R.string.judul_kosong)
       }

        if (binding.edtDeskripsiKegiatan.text.isEmpty()){
            showMessage(R.string.lokasi_kosong)
        }

        if (binding.edtLokasiKegiatan.text.isEmpty()){
            showMessage(R.string.lokasi_kosong)
        }

        val judul = binding.edtJudulKegiatan.text.toString()
        val deskripsi = binding.edtDeskripsiKegiatan.text.toString()
        val lokasi = binding.edtLokasiKegiatan.text.toString()

        val postingan = PostingData(
            "Akhdan Pangestuaji",
            judul,
            lokasi,
            deskripsi,
            currentDate
        )

        database = FirebaseDatabase.getInstance("https://aspirin-aspirasi-indonesia-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        database.child("postingan").child(database.push().key.toString())
            .setValue(postingan)

    }

    private fun showMessage(messageResId: Int) {
        Toast.makeText(applicationContext, messageResId, Toast.LENGTH_LONG).apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }
}