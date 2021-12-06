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
import androidx.navigation.fragment.findNavController
import org.d3ifcool.aspirin.data.viewmodel.authentication.AuthViewModel
import org.d3ifcool.aspirin.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkEmptyForm()
        checkRegisterStatus()
        binding.haveAccount.setOnClickListener {
            findNavController().popBackStack()
        }
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
        viewModel.getRegisterStatus().observe(viewLifecycleOwner, { registered ->
            if (registered) {
                Toast.makeText(
                    context,
                    "Register Success, silahkan verifikasi email anda",
                    Toast.LENGTH_LONG
                ).show()
                findNavController().popBackStack()
            } else {
                val message = viewModel.getErrMessage()
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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