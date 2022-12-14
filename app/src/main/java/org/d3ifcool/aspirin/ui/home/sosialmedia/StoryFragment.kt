package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
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
        viewModel.authUser.observe(viewLifecycleOwner, { getCurrentUser(it) })
        showLoading()

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

        binding.tvMenuLokasi.setOnClickListener {
            findNavController().navigate(R.id.action_storyFragment_to_locationFragment)
        }

        myadapter.setOnItemClickCallback(object : SosialMediaAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PostingData) {
                findNavController().navigate(
                    StoryFragmentDirections.actionStoryFragmentToCommentFragment(data)
                )
            }
        }
        )
        binding.accountIcon.setOnClickListener {
            findNavController().navigate(R.id.action_storyFragment_to_settingActivity)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_storyFragment_to_cameraFragment)
        }
    }

    private fun showLoading() {
        viewModel.getLoadStory().observe(viewLifecycleOwner, {
            if (it == true) {
                binding.progressbar.visibility = View.GONE
            }
        })
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

    private fun getCurrentUser(user: FirebaseUser?) {
        if (user != null) {
            binding.tvUsername.text = user.displayName.toString()
            userAuthenticated()
        } else {
            binding.tvUsername.text = getString(R.string.login)
            userNotAuthenticated()
        }
    }

    private fun userAuthenticated() {
        binding.accountIcon.setOnClickListener {
            findNavController().navigate(R.id.action_storyFragment_to_settingActivity)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_storyFragment_to_cameraFragment)
        }
    }

    private fun userNotAuthenticated() {
        binding.accountIcon.setOnClickListener {
            findNavController().navigate(R.id.action_storyFragment_to_loginFragment)
        }

        binding.fab.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Membuat Postingan")
                .setMessage("Silahkan melakukan login terlebih dahulu untuk membuat postingan")
                .setCancelable(false)
                .setPositiveButton(
                    "Login"
                ) { _, _ ->
                    findNavController().navigate(R.id.action_storyFragment_to_loginFragment)
                }
                .setNegativeButton(
                    "Batal"
                ) { _, _ -> }
                .show()
        }
    }

}