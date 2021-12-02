package org.d3ifcool.aspirin.data.viewmodel.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.aspirin.data.model.sosialmedia.MapData
import org.d3ifcool.aspirin.data.network.Repo

class LocationViewModel: ViewModel() {

    private val repository = Repo()

    fun fetchLocationData(): LiveData<MutableList<MapData>> {
        val mutableMapData = MutableLiveData<MutableList<MapData>>()
        repository.getMapData().observeForever { mapData ->
            mutableMapData.value = mapData
        }
        return mutableMapData
    }
}