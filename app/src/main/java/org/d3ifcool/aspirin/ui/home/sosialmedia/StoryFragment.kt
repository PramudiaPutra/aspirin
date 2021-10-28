package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.d3ifcool.aspirin.databinding.FragmentStoryBinding
import org.d3ifcool.aspirin.ui.camera.CameraActivity

class StoryFragment : Fragment() {

    private lateinit var binding: FragmentStoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryBinding.inflate(layoutInflater, container, false)

        binding.fab.setOnClickListener {
            val intent = Intent(context, CameraActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}