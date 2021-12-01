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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.d3ifcool.aspirin.utils.observeOnce

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
        var coordinate: LatLng? = null
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
        checkLocationServiceInitial()
        checkPostingStatus()
        shareLocation()
        showImage()

        binding.btnKirimKegiatan.setOnClickListener { posting() }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment

        mapFragment?.getMapAsync { googleMap ->
            val defaultJakarta = LatLng(-6.200000, 106.816666)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultJakarta, 8f))

            binding.btnEnableLocation.setOnClickListener {
                binding.btnEnableLocation.visibility = View.GONE
                binding.btnGetLocation.visibility = View.VISIBLE

                enableLocation()
                observeLocation(googleMap)

                viewModel.getLocationStatus().observe(viewLifecycleOwner, { getLocation ->
                    if (getLocation) {
                        binding.mapProgressbar.visibility = View.GONE
                    }
                })
            }

            binding.btnGetLocation.setOnClickListener {
                binding.mapProgressbar.visibility = View.VISIBLE

                observeLocation(googleMap)
            }
        }

        viewModel.authUser.observe(viewLifecycleOwner, { getCurrentUser(it) })
    }

    private fun observeLocation(googleMap: GoogleMap) {
        val coordinateObserver = viewModel.getCoordinate()
        coordinateObserver.observeOnce(viewLifecycleOwner, { getCoordinate ->
            if (getCoordinate != null) {
                binding.mapProgressbar.visibility = View.GONE
                coordinate = getCoordinate
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        viewModel.locationCoordinate.value!!,
                        14f
                    )
                )
            }
        })
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
                .setTitle("Layanan Lokasi")
                .setMessage("Izinkan pengunaan akses lokasi untuk mempermudah pencarian lokasi anda")
                .setPositiveButton(
                    "Ok"
                ) { _, _ ->
                    permissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
                .setNegativeButton(
                    "Tolak"
                ) { _, _ ->
                    binding.btnEnableLocation.visibility = View.VISIBLE
                    binding.btnGetLocation.visibility = View.GONE
                    binding.mapProgressbar.visibility = View.GONE
                }
                .show()
        }
        checkLocationService()
    }


    private fun checkLocationServiceInitial() {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationEnabled = try {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            false
        }

        if (locationEnabled && locationGranted) {
            binding.btnEnableLocation.visibility = View.GONE
            binding.btnGetLocation.visibility = View.VISIBLE
        }
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
                .setTitle("Lokasi Perangkat")
                .setMessage("Nyalakan lokasi perangkat untuk mendapatkan lokasi")
                .setPositiveButton(
                    "Ok"
                ) { _, _ ->
                    requireContext().startActivity(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    )
                    binding.mapProgressbar.visibility = View.VISIBLE
                }
                .setNegativeButton("Tolak") { _, _ ->
                    binding.btnEnableLocation.visibility = View.VISIBLE
                    binding.btnGetLocation.visibility = View.GONE
                }
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
                        lifecycleScope.cancel()
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