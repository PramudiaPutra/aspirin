package org.d3ifcool.aspirin.ui.authentication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3ifcool.aspirin.R

class PostingStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting_story)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
    }
}