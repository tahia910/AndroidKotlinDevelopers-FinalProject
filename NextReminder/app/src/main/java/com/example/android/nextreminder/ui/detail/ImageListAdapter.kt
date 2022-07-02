package com.example.android.nextreminder.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.nextreminder.data.ImageDTO
import com.example.android.nextreminder.databinding.GridImageItemBinding

class ImageListAdapter : ListAdapter<ImageDTO, ImageListAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<ImageDTO>() {
        override fun areContentsTheSame(oldItem: ImageDTO, newItem: ImageDTO): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: ImageDTO, newItem: ImageDTO): Boolean {
            return oldItem === newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private var binding: GridImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageItem: ImageDTO) {
            binding.image = imageItem
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GridImageItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}