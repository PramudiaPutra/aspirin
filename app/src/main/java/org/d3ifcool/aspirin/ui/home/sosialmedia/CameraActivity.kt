package org.d3ifcool.aspirin.ui.home.sosialmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3ifcool.aspirin.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        binding.camera.setLifecycleOwner(this)
        setContentView(binding.root)
    }
}