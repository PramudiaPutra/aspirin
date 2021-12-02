package org.d3ifcool.aspirin.data.model.comment
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentData(
    var key: String? = null,
    var userId: String? = null,
    var username: String? = null,
    var comment:String? = null,
    val date: String? = null,
) : Parcelable
