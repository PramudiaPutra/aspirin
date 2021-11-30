package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.aspirin.data.viewmodel.sosialmedia.PostingViewModel
import java.util.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.auth.FirebaseUser
import com.otaliastudios.cameraview.CameraUtils
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.databinding.FragmentPostingBinding
import org.d3ifcool.aspirin.ui.camera.PreviewFragment
import java.io.File
import java.text.SimpleDateFormat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PostingFragment : Fragment() {

    private lateinit var photoUri: Uri
    private lateinit var username: String
    private lateinit var permissionRequest: ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        var locationGranted = false
        var locationEnabled = false
        var lat: Double? = null
        var long: Double? = null
    }

    private lateinit var binding: FragmentPostingBinding
    private val viewModel: PostingViewModel by lazy {
        ViewModelProvider(this).get(PostingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLocationPermission()
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
        shareLocation()
        showImage()

        binding.btnKirimKegiatan.setOnClickListener { posting() }
        binding.btnEnableLocation.setOnClickListener {
            enableLocation()
            viewModel.getLocationStatus().observe(viewLifecycleOwner, { getLocation ->
                if (getLocation) {
                    binding.progressbar.visibility = View.GONE
                }
            })
        }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync {
        }

        viewModel.authUser.observe(viewLifecycleOwner, { getCurrentUser(it) })
    }

    private fun shareLocation() {
        binding.switchShareLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.mapFrame.visibility = View.VISIBLE
            } else {
                binding.mapFrame.visibility = View.GONE
            }
        }

    }

    private fun enableLocation() {
        if (!locationGranted) {
            AlertDialog.Builder(context)
                .setTitle("Akses Lokasi")
                .setMessage("Izinkan pengunaan akses lokasi untuk mempermudah pencarian lokasi anda")
                .setPositiveButton(
                    "Ok"
                ) { _, _ ->
                    binding.progressbar.visibility = View.VISIBLE
                    permissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
                .setNegativeButton(
                    "Tolak"
                ) { _, _ -> }
                .show()
        }
        binding.progressbar.visibility = View.VISIBLE
        checkLocationService()
    }

    private fun checkLocationService() {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationEnabled = try {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            false
        }

        if (!locationEnabled) {
            AlertDialog.Builder(context)
                .setMessage("Nyalakan lokasi perangkat untuk mendapatkan lokasi")
                .setPositiveButton(
                    "Ok"
                ) { _, _ ->
                    requireContext().startActivity(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    )
                }
                .setNegativeButton("Tolak", null)
                .show()
        }
    }

    private fun requestLocationPermission() {
        permissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    locationGranted = true
                    getMyLocation()
                } else {
                    locationGranted = false
                }
            }

        when (PackageManager.PERMISSION_GRANTED) {
            context?.let {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } -> {
                locationGranted = true
                getMyLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        val mLocationRequest = viewModel.getLocationRequest()
        val mLocationCallback = viewModel.getLocationCallback()

        LocationServices.getFusedLocationProviderClient(requireContext())
            .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())

        fusedLocationClient = LocationServices
            .getFusedLocationProviderClient(requireContext())

        lifecycleScope.launch {
            viewModel.getLocationStatus().asFlow().collectLatest { getLocation ->
                if (getLocation) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        lat = location.latitude
                        long = location.longitude
                        Toast.makeText(
                            context,
                            "lat: $lat  long: $long",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
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

        val judul = binding.edtJudulKegiatan.text.toString()
        val deskripsi = binding.edtDeskripsiKegiatan.text.toString()
        val lokasi = binding.edtLokasiKegiatan.text.toString()

        val file = File(
            requireContext().filesDir,
            "${System.currentTimeMillis()}_${UUID.randomUUID()}.jpg"
        )

        CameraUtils.writeToFile(PreviewFragment.pictureResult!!.data, file) {
            if (it != null) {
                val context = requireContext()
                photoUri =
                    FileProvider.getUriForFile(context, context.packageName + ".provider", file)
                viewModel.postData(username, judul, lokasi, deskripsi, currentDate, photoUri)
            } else {
                Toast.makeText(
                    context,
                    "Error while writing file.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showImage() {
        try {
            PreviewFragment.pictureResult?.toBitmap(
                1000,
                1000
            ) { bitmap -> binding.imgAddPosting.setImageBitmap(bitmap) }
        } catch (e: Exception) {
            Toast.makeText(context, "preview error $e", Toast.LENGTH_LONG).show()
        }
    }
}