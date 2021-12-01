package org.d3ifcool.aspirin.data.model.sosialmedia

import android.os.Parcelable
import org.d3ifcool.aspirin.data.model.comment.Comment
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostingData(
    val username: String? = null,
    val judulPosting: String? = null,
    val lokasiPosting: String? = null,
    val deskripsiPosting: String? = null,
    val tanggalPosting: String? = null,
    val photoUrl: String? = null,
    val comments: List<Comment>
): Parcelable
