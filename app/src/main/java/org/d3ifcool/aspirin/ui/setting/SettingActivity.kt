package org.d3ifcool.aspirin.ui.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.aspirin.data.viewmodel.authentication.AuthViewModel
import org.d3ifcool.aspirin.databinding.ActivitySettingBinding
import org.d3ifcool.aspirin.ui.authentication.login.LoginActivity

class SettingActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.auth.observe(this, { getCurrentUser(it) })

        binding.buttonKeluar.setOnClickListener {
            logOut()
        }
    }

    private fun getCurrentUser(user: FirebaseUser?) {
        if (user != null) {
            binding.tvUsername.text =  user.displayName.toString()
            binding.tvEmail.text = user.email.toString()
        }
    }

    private fun logOut() {
        AuthUI.getInstance().signOut(this)
        startActivity(Intent(this, LoginActivity::class.java))
    }
}