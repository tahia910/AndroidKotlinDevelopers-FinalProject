package com.example.android.nextreminder.utils

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.nextreminder.R
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.data.SimilarItemTypeEnum
import com.example.android.nextreminder.data.SimilarItemTypeEnum.*
import com.example.android.nextreminder.ui.SimilarListAdapter

@BindingAdapter("bindListData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<SimilarDTO>?) {
    val adapter = recyclerView.adapter as SimilarListAdapter
    adapter.submitList(data)
}

@BindingAdapter("isGone")
fun setViewVisibility(view: View, isNotVisible: Boolean) {
    view.visibility = if (isNotVisible) View.GONE else View.VISIBLE
}

@BindingAdapter("url")
fun setUrl(view: View, url: String) {
    if (url.isEmpty()) return
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (intent.resolveActivity(view.context.packageManager) != null) {
        view.setOnClickListener {
            view.context.startActivity(intent)
        }
    }
}

@BindingAdapter("typeIcon")
fun setTypeIcon(view: ImageView, type: SimilarItemTypeEnum) {
    val icon = when (type) {
        MUSIC -> R.drawable.ic_music
        MOVIE -> R.drawable.ic_movie
        TVSHOW -> R.drawable.ic_tv
        BOOK -> R.drawable.ic_book
        AUTHOR -> R.drawable.ic_person
        GAME -> R.drawable.ic_game
        PODCAST -> R.drawable.ic_radio
    }
    view.setImageResource(icon)
}