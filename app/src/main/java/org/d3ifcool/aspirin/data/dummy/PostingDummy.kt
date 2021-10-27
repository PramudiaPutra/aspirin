package org.d3ifcool.aspirin.data.dummy

import org.d3ifcool.aspirin.R
import org.d3ifcool.aspirin.data.viewmodel.sosialmedia.PostingData

class PostingDummy {
    companion object{
        fun dataSet(): ArrayList<PostingData> {
            val list = ArrayList<PostingData>()
            list.add(
                PostingData(
                    "Akhdan Pangestuaji",
                    R.drawable.aspirin_main_icon,
                    R.drawable.hint_rv,
                    "Ini Judul 1",
                    "Purwokerto",
                    "Ini adalah deskripsi",
                    "27/10/2021"
                )
            )

            list.add(
                PostingData(
                    "Akhdan Pangestuaji",
                    R.drawable.aspirin_main_icon,
                    R.drawable.hint_rv,
                    "Ini Judul 2",
                    "Purwokerto",
                    "Ini adalah deskripsi",
                    "27/10/2021"
                )
            )
            return list
        }
    }
}