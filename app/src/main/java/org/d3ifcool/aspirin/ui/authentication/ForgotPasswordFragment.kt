package org.d3ifcool.aspirin.ui.authentication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.aspirin.data.viewmodel.authentication.AuthViewModel
import org.d3ifcool.aspirin.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkEmptyForm()
        checkResetPasswordStatus()

        binding.buttonReset.setOnClickListener { resetPassword() }
    }

    private fun checkEmptyForm() {
        with(binding) {
            buttonReset.isEnabled = false
            val editTexts = listOf(edtEmail)
            for (editText in editTexts) {
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val edtEmail = edtEmail.text.toString().trim()

                        buttonReset.isEnabled = edtEmail.isNotEmpty()
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                })
            }
        }
    }

    private fun checkResetPasswordStatus() {
        viewModel.getResetPasswordStatus().observe(viewLifecycleOwner, { resetPassword ->
            if (resetPassword) {
                Toast.makeText(
                    context,
                    "Permintaan reset password telah terkirim, silahkan cek email anda",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val message = viewModel.getErrMessage()
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                binding.progressbar.visibility = View.GONE
            }

        })
    }

    private fun resetPassword() {
        val email = binding.edtEmail.text.toString()
        viewModel.resetPassword(email)
    }
}