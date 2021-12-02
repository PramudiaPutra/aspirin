package org.d3ifcool.aspirin.data.model.sosialmedia

import android.os.Parcelable
import org.d3ifcool.aspirin.data.model.comment.CommentData
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostingData(
    var key: String? = null,
    val username: String? = null,
    val judulPosting: String? = null,
    val lokasiPosting: String? = null,
    val deskripsiPosting: String? = null,
    val tanggalPosting: String? = null,
    var photoUrl: String? = null,
    val comments: List<CommentData>? = null,
) : Parcelable
