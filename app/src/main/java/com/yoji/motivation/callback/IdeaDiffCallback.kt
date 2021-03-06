package com.yoji.motivation.callback

import androidx.recyclerview.widget.DiffUtil
import com.yoji.motivation.dto.Idea
import com.yoji.motivation.dto.IdeaWithAuthor

class IdeaDiffCallback : DiffUtil.ItemCallback<IdeaWithAuthor>() {
    override fun areItemsTheSame(oldItem: IdeaWithAuthor, newItem: IdeaWithAuthor): Boolean {
        return oldItem.idea.id == newItem.idea.id
    }

    override fun areContentsTheSame(oldItem: IdeaWithAuthor, newItem: IdeaWithAuthor): Boolean {
        return oldItem == newItem
    }
}