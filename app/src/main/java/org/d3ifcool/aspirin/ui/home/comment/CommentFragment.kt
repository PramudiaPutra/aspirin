package org.d3ifcool.aspirin.ui.home.comment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.model.comment.CommentData
import org.d3ifcool.aspirin.data.viewmodel.sosialmedia.CommentViewModel
import org.d3ifcool.aspirin.databinding.FragmentCommentBinding
import java.text.SimpleDateFormat
import java.util.*

class CommentFragment : Fragment() {

    private lateinit var binding: FragmentCommentBinding
    private lateinit var myadapter: CommentAdapter
    private lateinit var key: String
    private var userId: String? = null
    private var username: String? = null

    private val viewModel: CommentViewModel by lazy {
        ViewModelProvider(this).get(CommentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUserBeforeComment()
        checkEmptyForm()
        getArgsData()

        binding.buttonComment.setOnClickListener {
            addComment()
            binding.edtComment.text?.clear()
        }

        viewModel.authUser.observe(viewLifecycleOwner, { getCurrentUser(it) })

        myadapter = CommentAdapter()
        with(binding.recyclerViewComment) {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true

            layoutManager = linearLayoutManager
            observeData()
            setHasFixedSize(true)
            adapter = myadapter
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        viewModel.fetchCommentData(key).observe(
            viewLifecycleOwner, {
                myadapter.setListData(it)
                myadapter.notifyDataSetChanged()
            }
        )
    }

    private fun getArgsData() {
        val args = CommentFragmentArgs.fromBundle(requireArguments())
        key = args.posting.key.toString()

        Glide.with(binding.imgComment.context).load(args.posting.photoUrl).into(binding.imgComment)
        binding.tvNamaUser.text = args.posting.username
        binding.tvLokasiPosting.text = args.posting.lokasiPosting
        binding.tvTanggalPosting.text = args.posting.tanggalPosting
    }


    private fun addComment() {
        val comment = binding.edtComment.text.toString()
        val sdf = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())

        val commentData = CommentData(
            key,
            userId,
            username,
            comment,
            currentDate
        )
        viewModel.postComment(commentData)
    }

    private fun checkEmptyForm() {
        with(binding) {
            buttonComment.isEnabled = false
            val editTexts = listOf(edtComment)

            for (editText in editTexts) {
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val edtComment = edtComment.text.toString().trim()
                        buttonComment.isEnabled = edtComment.isNotEmpty()
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                })
            }
        }
    }

    private fun getCurrentUser(user: FirebaseUser?) {
        if (user != null) {
            username = user.displayName.toString()
            userId = user.uid
        }
    }

    private fun checkUserBeforeComment() {
        binding.edtComment.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (userId == null && username == null) {
                        AlertDialog.Builder(context)
                            .setMessage("Silahkan melakukan login terlebih dahulu untuk memberikan comment")
                            .setCancelable(false)
                            .setPositiveButton(
                                "Login"
                            ) { _, _ ->
                                findNavController().navigate(R.id.action_commentFragment_to_loginFragment)
                            }
                            .setNegativeButton(
                                "Batal"
                            ) { _, _ ->
                                binding.edtComment.text?.clear()
                                binding.edtComment.clearFocus()
                            }
                            .show()
                    }
                }
            }
    }
}