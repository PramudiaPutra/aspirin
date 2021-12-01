package org.d3ifcool.aspirin.data.model.comment

data class Comment(
    var userId: String = "",
    var photoUser: Int = 0,
    var username: String = "",
    var commentContent: String = ""
)
