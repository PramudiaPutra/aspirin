package org.d3ifcool.aspirin.data.model.comment

import org.d3ifcool.aspirin.R

object CommentData {
    private val userId = arrayOf(
        "id1",
        "id2",
        "id3",
        "id4",
        "id5"
    )
    private val photoUser = intArrayOf(
        R.drawable.aspirin_main_icon,
        R.drawable.aspirin_main_icon,
        R.drawable.aspirin_main_icon,
        R.drawable.aspirin_main_icon,
        R.drawable.aspirin_main_icon,
    )

    private val userName = arrayOf(
        "Akhdan Pangestuaji",
        "Cahya Aulia Putri",
        "Pramudia Putra",
        "Widodo Aji",
        "Caul"
        )
    private val contentComment = arrayOf(
        "Yuk bantuin",
        "Wahh kejadianya kapan tuh",
        "Barusan aja mba, langsung macet",
        "Gas lah auto gotong-royong",
        "Emang harus tau nih pemerintah"
    )

    val listData: ArrayList<Comment>
        get() {
            val list = arrayListOf<Comment>()
            for (position in photoUser.indices) {
                val comment = Comment()
                comment.userId = userId[position]
                comment.photoUser = photoUser[position]
                comment.username = userName[position]
                comment.commentContent = contentComment[position]
                list.add(comment)
            }
            return list
        }
}