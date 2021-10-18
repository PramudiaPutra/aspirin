package org.d3ifcool.aspirin.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3ifcool.aspirin.databinding.ActivityLoginBinding
import org.d3ifcool.aspirin.databinding.ActivitySplashScreenBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}