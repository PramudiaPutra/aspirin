package org.d3ifcool.aspirin.data.model.sosialmedia

data class PostingData(
    val username: String? = null,
    val judulPosting: String? = null,
    val lokasiPosting: String? = null,
    val deskripsiPosting: String? = null,
    val tanggalPosting: String? = null,
    val photoUrl: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
)
