package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.model.sosialmedia.PostingData
import org.d3ifcool.aspirin.data.viewmodel.sosialmedia.PostingViewModel
import org.d3ifcool.aspirin.databinding.FragmentStoryBinding


class StoryFragment : Fragment() {

    private lateinit var binding: FragmentStoryBinding
    private lateinit var myadapter: SosialMediaAdapter
    private val viewModel: PostingViewModel by lazy {
        ViewModelProvider(this).get(PostingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myadapter = SosialMediaAdapter()
        with(binding.recyclerView) {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true

            layoutManager = linearLayoutManager
            observeData()
            setHasFixedSize(true)
            adapter = myadapter
        }

        myadapter.setOnItemClickCallback(object : SosialMediaAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PostingData) {
                findNavController().navigate(
                    StoryFragmentDirections.actionStoryFragmentToCommentFragment(data)
                )
            }
        }
        )
        binding.aspirinIcon.setOnClickListener {
            findNavController().navigate(R.id.action_storyFragment_to_settingActivity)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_storyFragment_to_cameraFragment)
        }
    }

    private fun showSelectedHero(data: PostingData) {
        Toast.makeText(context, "Kamu memilih " + data.deskripsiPosting, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        viewModel.fetchPostingData().observe(
            viewLifecycleOwner, {
                myadapter.setListData(it)
                myadapter.notifyDataSetChanged()
            }
        )
    }
}