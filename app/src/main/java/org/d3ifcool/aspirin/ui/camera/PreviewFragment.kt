package org.d3ifcool.aspirin.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.otaliastudios.cameraview.PictureResult
import org.d3ifcool.aspirin.databinding.FragmentPreviewBinding

class PreviewFragment : Fragment() {

    companion object {
        var pictureResult: PictureResult? = null
    }

    private lateinit var binding: FragmentPreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.confirmPhotoButton.setOnClickListener {
        }

        try {
            pictureResult?.toBitmap(1000, 1000) { bitmap ->
                binding.photoPreview.setImageBitmap(bitmap)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "preview error $e", Toast.LENGTH_LONG).show()
        }
    }
}