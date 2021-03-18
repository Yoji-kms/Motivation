package com.yoji.motivation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.yoji.motivation.callback.AuthorDiffCallback
import com.yoji.motivation.databinding.ItemAuthorBinding
import com.yoji.motivation.dto.Author
import com.yoji.motivation.listeners.OnAuthorClickListener
import com.yoji.motivation.viewholder.AuthorViewHolder

class AuthorAdapter(
    private val onAuthorClickListener: OnAuthorClickListener,
    private val currentAuthorId: Long
) : PagingDataAdapter<Author, AuthorViewHolder>(AuthorDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {
        val binding = ItemAuthorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AuthorViewHolder(
            binding = binding,
            onAuthorClickListener = onAuthorClickListener,
            currentAuthorId = currentAuthorId
        )
    }

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {
        val author = getItem(position)
        holder.bind(author!!)
    }
}