package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.viewmodel.sosialmedia.PostingData
import org.d3ifcool.aspirin.databinding.ItemCardviewPostinganBinding

class SosialMediaAdapter : RecyclerView.Adapter<SosialMediaAdapter.ViewHolder>() {
    private var listData = mutableListOf<PostingData>()
    fun setListData(data:MutableList<PostingData>){
        listData = data
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SosialMediaAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardviewPostinganBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SosialMediaAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ViewHolder(private val binding: ItemCardviewPostinganBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataPosting: PostingData) = with(binding) {
            tvNamaUser.text = dataPosting.username
            tvTanggalPosting.text = dataPosting.tanggalPosting
            tvLokasiPosting.text = dataPosting.lokasiPosting
            tvTitlePosting.text = dataPosting.judulPosting
            tvKontenPosting.text = dataPosting.deskripsiPosting
            Glide.with(profileImage.context).load(R.drawable.aspirin_main_icon).into(profileImage)

            val requestOption = RequestOptions()
                .placeholder(R.drawable.icon_image)
                .error(R.drawable.icon_broken_image)

            Glide.with(imgPosting.context)
                .applyDefaultRequestOptions(requestOption)
                .load(dataPosting.photoUrl)
                .into(imgPosting)
        }
    }

}