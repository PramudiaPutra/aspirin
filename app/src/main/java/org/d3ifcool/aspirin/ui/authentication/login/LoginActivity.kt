package org.d3ifcool.aspirin.ui.authentication.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.aspirin.data.viewmodel.authentication.AuthViewModel
import org.d3ifcool.aspirin.databinding.ActivityLoginBinding
import org.d3ifcool.aspirin.ui.authentication.RegisterActivity
import org.d3ifcool.aspirin.ui.home.sosialmedia.SosialMediaActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val contract = FirebaseAuthUIActivityResultContract()
    private val signInLauncher = registerForActivityResult(contract) {}

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkEmptyForm()
        checkLoginStatus()

        binding.buttonGoogleLogin.setOnClickListener { googleLogin() }
        binding.buttonLogin.setOnClickListener { emailLogin() }
        binding.toRegister.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }

        viewModel.auth.observe(this, { authState(it) })
    }

    private fun checkEmptyForm() {
        with(binding) {
            buttonLogin.isEnabled = false
            val editTexts = listOf(edtEmail, edtPassword)
            for (editText in editTexts) {
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val edtEmail = edtEmail.text.toString().trim()
                        val edtPassword = edtPassword.text.toString().trim()

                        buttonLogin.isEnabled = (edtEmail.isNotEmpty()
                                && edtPassword.isNotEmpty())
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                })
            }
        }
    }

    private fun authState(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, SosialMediaActivity::class.java))
        }
    }

    private fun checkLoginStatus() {
        viewModel.getLoginStatus().observe(this, { loginSuccess ->
            if (!loginSuccess) {
                val message = viewModel.getErrMessage()
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                binding.progressbar.visibility = View.GONE
            }
        })
    }

    private fun googleLogin() {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(intent)
    }

    private fun emailLogin() {
        binding.progressbar.visibility = View.VISIBLE
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        viewModel.login(email, password)
    }
}