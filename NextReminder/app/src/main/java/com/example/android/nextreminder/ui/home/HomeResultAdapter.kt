package com.example.android.nextreminder.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.databinding.ListItemBinding

class HomeResultAdapter : ListAdapter<SimilarDTO, HomeResultAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<SimilarDTO>() {
        override fun areContentsTheSame(oldItem: SimilarDTO, newItem: SimilarDTO): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: SimilarDTO, newItem: SimilarDTO): Boolean {
            return oldItem === newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private var binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dtoItem: SimilarDTO) {
            binding.similarItem = dtoItem
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}