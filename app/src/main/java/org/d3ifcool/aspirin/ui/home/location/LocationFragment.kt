package org.d3ifcool.aspirin.ui.home.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.d3ifcool.aspirin.databinding.FragmentLocationsBinding

class LocationFragment : Fragment() {

    private lateinit var binding: FragmentLocationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationsBinding.inflate(layoutInflater)
        return binding.root
    }
}