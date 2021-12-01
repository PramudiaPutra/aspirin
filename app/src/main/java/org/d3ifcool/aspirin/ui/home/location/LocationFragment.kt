package org.d3ifcool.aspirin.ui.home.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.addMarker
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.model.sosialmedia.MapData
import org.d3ifcool.aspirin.data.viewmodel.location.LocationViewModel
import org.d3ifcool.aspirin.databinding.FragmentLocationsBinding

class LocationFragment : Fragment() {

    private lateinit var binding: FragmentLocationsBinding

    private val viewModel: LocationViewModel by lazy {
        ViewModelProvider(this).get(LocationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment_location) as? SupportMapFragment

        mapFragment?.getMapAsync { googleMap ->
            googleMap.setOnMapLoadedCallback {
                viewModel.fetchLocationData().observe(viewLifecycleOwner, { mapData ->
                    addMarker(mapData, googleMap)
                })
            }
        }
    }

    private fun addMarker(mapData: MutableList<MapData>, googleMap: GoogleMap) {
        val bound = LatLngBounds.builder()

        mapData.forEach {
            val location = LatLng(it.lat!!, it.lon!!)

            bound.include(location)
            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(it.title)
            )
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bound.build(), 20))
    }
}