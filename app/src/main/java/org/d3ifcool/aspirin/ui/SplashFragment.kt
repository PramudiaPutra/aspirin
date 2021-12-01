package org.d3ifcool.aspirin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.viewmodel.authentication.AuthViewModel
import org.d3ifcool.aspirin.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.auth.observe(viewLifecycleOwner, { authState(it) })
    }

    private fun authState(user: FirebaseUser?) {
        if (user != null) {
            findNavController().navigate(R.id.action_splashFragment_to_storyFragment)
        } else {
            findNavController().navigate(R.id.action_splash_to_loginFragment)
        }
    }
}