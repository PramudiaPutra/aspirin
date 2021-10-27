package org.d3ifcool.aspirin.ui.home.sosialmedia

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.viewmodel.sosialmedia.PostingData
import org.d3ifcool.aspirin.databinding.ItemCardviewPostinganBinding

class SosialMediaAdapter(private val listData: ArrayList<PostingData>) : RecyclerView.Adapter<SosialMediaAdapter.ViewHolder>() {
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
            Glide.with(profileImage.context).load(dataPosting.imgUser).into(profileImage)
            Glide.with(imgPosting.context).load(dataPosting.imgPosting).into(imgPosting)
        }
    }

}