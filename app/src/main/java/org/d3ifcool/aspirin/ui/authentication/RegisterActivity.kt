package org.d3ifcool.aspirin.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.aspirin.data.viewmodel.authentication.AuthViewModel
import org.d3ifcool.aspirin.databinding.ActivityRegisterBinding
import org.d3ifcool.aspirin.ui.authentication.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.haveAccount.setOnClickListener { onBackPressed() }
        binding.buttonRegister.setOnClickListener { registerUser() }

        viewModel.getRegisterStatus().observe(this, { registered ->
            if (registered) {
                Toast.makeText(
                    this,
                    "Register Success",
                    Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                val message = viewModel.getErrMessage()
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                binding.progressbar.visibility = View.GONE
            }
        })
    }

    private fun registerUser() {
        binding.progressbar.visibility = View.VISIBLE
        val username = binding.edtUsername.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        viewModel.register(username, email, password)
    }
}