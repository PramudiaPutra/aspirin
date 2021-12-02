package org.d3ifcool.aspirin.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.viewmodel.authentication.AuthViewModel
import org.d3ifcool.aspirin.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.auth.observe(viewLifecycleOwner, { getCurrentUser(it) })

        binding.buttonKeluar.setOnClickListener {
            logOut()
        }
        binding.buttonKebijakanPrivasi.setOnClickListener{
            findNavController().navigate(R.id.action_settingActivity_to_privasiFragment)
        }
        binding.buttonTentangAspirin.setOnClickListener{
            findNavController().navigate(R.id.action_settingActivity_to_aboutFragment)
        }
    }

    private fun getCurrentUser(user: FirebaseUser?) {
        if (user != null) {
            binding.tvUsername.text = user.displayName.toString()
            binding.tvEmail.text = user.email.toString()
        }
    }

    private fun logOut() {
        AuthUI.getInstance().signOut(requireContext())
        findNavController().navigate(R.id.action_settingActivity_to_loginFragment)
    }
}