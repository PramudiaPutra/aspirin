package org.d3ifcool.aspirin.data.model.sosialmedia

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapData(
    var key: String? = null,
    val title: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
) : Parcelable