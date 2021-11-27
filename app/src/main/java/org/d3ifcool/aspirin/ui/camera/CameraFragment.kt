package org.d3ifcool.aspirin.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.camera.setLifecycleOwner(viewLifecycleOwner)

        binding.camera.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)

                PreviewFragment.pictureResult = result
                findNavController().navigate(R.id.action_cameraFragment_to_previewFragment)
            }
        })

        binding.exitButton.setOnClickListener { findNavController().popBackStack() }
        binding.switchCameraButton.setOnClickListener { switchCamera() }
        binding.takePictureButton.setOnClickListener { capturePhotos() }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.camera.open()
    }

    private fun switchCamera() {
        with(binding) {
            if (camera.facing == Facing.BACK) {
                camera.facing = Facing.FRONT
            } else if (camera.facing == Facing.FRONT) {
                camera.facing = Facing.BACK
            }
        }
    }

    private fun capturePhotos() {
        binding.camera.takePictureSnapshot()
    }
}