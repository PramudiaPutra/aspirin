package org.d3ifcool.aspirin.ui.home.comment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.model.comment.Comment
import org.d3ifcool.aspirin.data.model.comment.CommentData
import org.d3ifcool.aspirin.databinding.FragmentCommentBinding
import org.d3ifcool.aspirin.databinding.FragmentStoryBinding
import org.d3ifcool.aspirin.ui.home.sosialmedia.SosialMediaAdapter

class CommentFragment : Fragment() {

    private lateinit var binding: FragmentCommentBinding
    private var list: ArrayList<Comment> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = CommentFragmentArgs.fromBundle(requireArguments())

        Glide.with(binding.imgComment.context).load(args.posting.photoUrl).into(binding.imgComment)
        Glide.with(binding.profileImage.context).load(R.drawable.aspirin_main_icon).into(binding.profileImage)
        binding.tvNamaUser.text = args.posting.username
        binding.tvLokasiPosting.text = args.posting.lokasiPosting
        binding.tvTanggalPosting.text = args.posting.tanggalPosting

        list.addAll(CommentData.listData)
        with(binding.recyclerViewComment){
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            val listCommentAdapter = CommentAdapter(list)
            adapter = listCommentAdapter
        }
    }
}