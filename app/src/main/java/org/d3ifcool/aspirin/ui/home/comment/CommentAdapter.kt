package org.d3ifcool.aspirin.ui.home.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3ifcool.aspirin.data.model.comment.CommentData
import org.d3ifcool.aspirin.databinding.ItemCommentBinding

//class CommentAdapter(private val listComment: ArrayList<Comment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
class CommentAdapter() : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    private var listComment = mutableListOf<CommentData>()
    fun setListData(data:MutableList<CommentData>){
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
        fun bind(data: CommentData) = with(binding) {
//            Glide.with(profileImageComment.context).load(data.photoUser).into(profileImageComment)
//            tvNamaUserComment.text = data.username
//            tvMessage.text = data.commentContent
        }

    }
}