package org.d3ifcool.aspirin.ui.authentication

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.viewmodel.authentication.AuthViewModel
import org.d3ifcool.aspirin.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val contract = FirebaseAuthUIActivityResultContract()
    private val signInLauncher = registerForActivityResult(contract) {}

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkEmptyForm()
        checkLoginStatus()
        showPrivacyPolicy(binding.tvPrivacyPolicy)

        binding.buttonGoogleLogin.setOnClickListener { googleLogin() }
        binding.buttonLogin.setOnClickListener { emailLogin() }
        binding.toRegister.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_registerFragment
            )
        }
        binding.textForgotPassword.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_forgotPasswordFragment
            )
        }

        viewModel.auth.observe(viewLifecycleOwner, { authState(it) })
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

                        buttonLogin.isEnabled =
                            (edtEmail.isNotEmpty() && edtPassword.isNotEmpty())
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                })
            }
        }
    }

    private fun authState(user: FirebaseUser?) {
        if (user != null) {
            if (user.isEmailVerified) {
                findNavController().navigate(R.id.action_loginFragment_to_storyFragment)
            }
        }
    }

    private fun checkLoginStatus() {
        viewModel.getLoginStatus().observe(viewLifecycleOwner, { loginSuccess ->
            if (loginSuccess) {
                viewModel.getVerifiedStatus().observe(viewLifecycleOwner, { verified ->
                    if (verified) {
                        findNavController().navigate(R.id.action_loginFragment_to_storyFragment)
                    } else {
                        binding.progressbar.visibility = View.GONE
                    }
                })
            } else {
                val message = viewModel.getErrMessage()
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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


    private fun showPrivacyPolicy(textView: TextView) {
        val content = getString(R.string.privacy_policy)
        val url = getString(R.string.privacy_policy_link)

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }
        val startIndex = content.indexOf(getString(R.string.click_privacy_policy))
        val endIndex = startIndex + getString(R.string.click_privacy_policy).length
        val spannableString = SpannableString(content)
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        with(textView) {
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }
}