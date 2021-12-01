package org.d3ifcool.aspirin.ui.home.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.model.comment.Comment
import org.d3ifcool.aspirin.data.model.sosialmedia.PostingData
import org.d3ifcool.aspirin.databinding.ItemCardviewPostinganBinding
import org.d3ifcool.aspirin.databinding.ItemCommentBinding

//class CommentAdapter(private val listComment: ArrayList<Comment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
class CommentAdapter() : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    private var listComment = mutableListOf<Comment>()
    fun setListData(data:MutableList<Comment>){
        listComment = data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCommentBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listComment[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return listComment.size
    }

    inner class ViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Comment) = with(binding) {
            Glide.with(profileImageComment.context).load(data.photoUser).into(profileImageComment)
            tvNamaUserComment.text = data.username
            tvMessage.text = data.commentContent
        }

    }
}