package org.d3ifcool.aspirin.data.model.sosialmedia

import org.d3ifcool.aspirin.data.model.comment.Comment

data class PostingData(
    val username: String? = null,
    val judulPosting: String? = null,
    val lokasiPosting: String? = null,
    val deskripsiPosting: String? = null,
    val tanggalPosting: String? = null,
    val photoUrl: String? = null,
)
