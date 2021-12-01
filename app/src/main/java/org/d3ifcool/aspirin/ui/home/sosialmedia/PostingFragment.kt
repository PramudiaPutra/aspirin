package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.aspirin.data.viewmodel.sosialmedia.PostingViewModel
import java.util.*
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import com.otaliastudios.cameraview.CameraUtils
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.model.comment.Comment
import org.d3ifcool.aspirin.databinding.FragmentPostingBinding
import org.d3ifcool.aspirin.ui.camera.PreviewFragment
import java.io.File
import java.text.SimpleDateFormat

class PostingFragment : Fragment() {

    private lateinit var photoUri: Uri
    private lateinit var username: String

    private lateinit var binding: FragmentPostingBinding
    private val viewModel: PostingViewModel by lazy {
        ViewModelProvider(this).get(PostingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPostingStatus()
        binding.btnKirimKegiatan.setOnClickListener { posting() }

        try {
            PreviewFragment.pictureResult?.toBitmap(
                1000,
                1000
            ) { bitmap -> binding.imgAddPosting.setImageBitmap(bitmap) }
        } catch (e: Exception) {
            Toast.makeText(context, "preview error $e", Toast.LENGTH_LONG).show()
        }

        viewModel.authUser.observe(viewLifecycleOwner, { getCurrentUser(it) })
    }

    private fun checkPostingStatus() {
        viewModel.getPostingStatus().observe(viewLifecycleOwner, { postingSuccess ->
            if (postingSuccess) {
                binding.progressbar.visibility = View.INVISIBLE
                findNavController().navigate(R.id.action_postingFragment_to_storyFragment)
            } else {
                Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun getCurrentUser(user: FirebaseUser?) {
        username = user!!.displayName.toString()
    }

    private fun posting() {
        binding.progressbar.visibility = View.VISIBLE

        val sdf = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())

        if (binding.edtJudulKegiatan.text.isEmpty()) {
            showMessage(R.string.judul_kosong)
        }

        if (binding.edtDeskripsiKegiatan.text.isEmpty()) {
            showMessage(R.string.lokasi_kosong)
        }

        if (binding.edtLokasiKegiatan.text.isEmpty()) {
            showMessage(R.string.lokasi_kosong)
        }

        val judul = binding.edtJudulKegiatan.text.toString()
        val deskripsi = binding.edtDeskripsiKegiatan.text.toString()
        val lokasi = binding.edtLokasiKegiatan.text.toString()

        val file = File(
            requireContext().filesDir,
            "${System.currentTimeMillis()}_${UUID.randomUUID()}.jpg"
        )

        val comment = listOf(Comment("123",0,"Akhdan","Iyaa"))

        CameraUtils.writeToFile(PreviewFragment.pictureResult!!.data, file) {
            if (it != null) {
                val context = requireContext()
                photoUri =
                    FileProvider.getUriForFile(context, context.packageName + ".provider", file)
                viewModel.postData(username, judul, lokasi, deskripsi, currentDate, photoUri, comment)
            } else {
                Toast.makeText(
                    context,
                    "Error while writing file.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showMessage(messageResId: Int) {
        Toast.makeText(context, messageResId, Toast.LENGTH_LONG).apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }
}