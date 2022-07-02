package com.example.android.nextreminder.utils

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.nextreminder.R
import com.example.android.nextreminder.data.ImageDTO
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.data.SimilarItemTypeEnum
import com.example.android.nextreminder.data.SimilarItemTypeEnum.*
import com.example.android.nextreminder.ui.SimilarListAdapter
import com.example.android.nextreminder.ui.detail.ImageListAdapter

@BindingAdapter("bindListData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<SimilarDTO>?) {
    val adapter = recyclerView.adapter as SimilarListAdapter
    adapter.submitList(data)
}

@BindingAdapter("bindGridData")
fun bindGridView(gridView: RecyclerView, data: List<ImageDTO>?) {
    if (data.isNullOrEmpty()) return
    val adapter = gridView.adapter as ImageListAdapter
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

@BindingAdapter("bookmarkContentDescription")
fun setContentDescription(view: View, item: SimilarDTO) {
    val string = if (item.isBookmarked) {
        R.string.content_description_remove_bookmark
    } else {
        R.string.content_description_add_bookmark
    }
    val contentDescription = view.context.getString(string, item.name)
    view.contentDescription = contentDescription
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

@BindingAdapter("image")
fun setGridImage(view: ImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .placeholder(R.drawable.ic_image)
        .error(R.drawable.ic_image_error)
        .centerCrop()
        .into(view)
}