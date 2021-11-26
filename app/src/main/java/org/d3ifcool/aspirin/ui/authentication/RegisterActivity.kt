package org.d3ifcool.aspirin.ui.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        checkEmptyForm()
        checkRegisterStatus()

        binding.haveAccount.setOnClickListener { onBackPressed() }
        binding.buttonRegister.setOnClickListener { registerUser() }
    }

    private fun checkEmptyForm() {
        with(binding) {
            buttonRegister.isEnabled = false
            val editTexts = listOf(edtUsername, edtEmail, edtPassword)
            for (editText in editTexts) {
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val edtUsername = edtUsername.text.toString().trim()
                        val edtEmail = edtEmail.text.toString().trim()
                        val edtPassword = edtPassword.text.toString().trim()

                        buttonRegister.isEnabled = (edtUsername.isNotEmpty()
                                && edtEmail.isNotEmpty()
                                && edtPassword.isNotEmpty())
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                })
            }
        }
    }

    private fun checkRegisterStatus() {
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