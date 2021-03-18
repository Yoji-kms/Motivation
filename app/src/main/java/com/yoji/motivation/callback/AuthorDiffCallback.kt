package com.yoji.motivation.callback

import androidx.recyclerview.widget.DiffUtil
import com.yoji.motivation.dto.Author

class AuthorDiffCallback : DiffUtil.ItemCallback<Author>() {
    override fun areItemsTheSame(oldItem: Author, newItem: Author): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Author, newItem: Author): Boolean {
        return oldItem == newItem
    }
}