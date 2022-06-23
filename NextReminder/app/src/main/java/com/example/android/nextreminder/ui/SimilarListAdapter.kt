package com.example.android.nextreminder.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.databinding.ListItemBinding

class SimilarListAdapter(private val clickListener: ItemClickListener) :
    ListAdapter<SimilarDTO, SimilarListAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<SimilarDTO>() {
        override fun areContentsTheSame(oldItem: SimilarDTO, newItem: SimilarDTO): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: SimilarDTO, newItem: SimilarDTO): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }

    class ViewHolder(private var binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: ItemClickListener, dtoItem: SimilarDTO) {
            binding.similarItem = dtoItem
            binding.clickListener = listener
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

class ItemClickListener(val clickListener: (item: SimilarDTO) -> Unit) {
    fun onClick(item: SimilarDTO) = clickListener(item)
}