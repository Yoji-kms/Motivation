package com.yoji.motivation.callback

import androidx.recyclerview.widget.DiffUtil
import com.yoji.motivation.dto.Idea

class IdeaDiffCallback : DiffUtil.ItemCallback<Idea>() {
    override fun areItemsTheSame(oldItem: Idea, newItem: Idea): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Idea, newItem: Idea): Boolean {
        return oldItem == newItem
    }
}