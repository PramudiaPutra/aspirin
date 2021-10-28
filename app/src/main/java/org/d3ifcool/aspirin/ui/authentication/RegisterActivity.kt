package org.d3ifcool.aspirin.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3ifcool.aspirin.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.haveAccount.setOnClickListener {
            onBackPressed()
        }
    }
}