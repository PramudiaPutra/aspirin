package org.d3ifcool.aspirin.data.model.comment
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    var userId: String = "",
    var photoUser: Int = 0,
    var username: String = "",
    var commentContent: String = ""
) : Parcelable
