package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3ifcool.aspirin.databinding.ActivitySosialMediaBinding
import org.d3ifcool.aspirin.ui.setting.SettingActivity

class SosialMediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySosialMediaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySosialMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.aspirinIcon.setOnClickListener {
            startActivity(
                Intent(this, SettingActivity::class.java)
            )
        }

    }
}