package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.databinding.ActivityPostingStoryBinding

class PostingStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostingStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostingStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnKirimKegiatan.setOnClickListener {
            posting()
        }
    }

    private fun posting(){
        Log.d("PostingStoryActivity", "Button kirim di klik")
    }

}