package com.yoji.motivation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.yoji.motivation.callback.IdeaDiffCallback
import com.yoji.motivation.databinding.ItemIdeaBinding
import com.yoji.motivation.dto.IdeaWithAuthor
import com.yoji.motivation.listeners.OnIdeaClickListener
import com.yoji.motivation.viewholder.IdeaViewHolder

class IdeaAdapter(
    private val onIdeaClickListener: OnIdeaClickListener
) : PagingDataAdapter<IdeaWithAuthor, IdeaViewHolder>(IdeaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaViewHolder {
        val binding = ItemIdeaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IdeaViewHolder(
            binding = binding,
            onIdeaClickListener = onIdeaClickListener
        )
    }

    override fun onBindViewHolder(holder: IdeaViewHolder, position: Int) {
        val idea = getItem(position)
        if (idea == null)
            holder.bindPlaceholder()
        else holder.bind(idea)
    }
}