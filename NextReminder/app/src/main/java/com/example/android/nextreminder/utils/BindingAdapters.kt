package com.example.android.nextreminder.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.ui.home.HomeResultAdapter

@BindingAdapter("bindListData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<SimilarDTO>?) {
    val adapter = recyclerView.adapter as HomeResultAdapter
    adapter.submitList(data)
}