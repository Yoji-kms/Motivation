package com.yoji.motivation.viewholder

import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.yoji.motivation.databinding.ItemAuthorBinding
import com.yoji.motivation.dto.Author
import com.yoji.motivation.listeners.OnAuthorClickListener
import kotlinx.coroutines.launch

class AuthorViewHolder(
    private val binding: ItemAuthorBinding,
    private val onAuthorClickListener: OnAuthorClickListener,
    private val currentAuthorId: Long
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(author: Author) {
        binding.apply {
            authorNameTxtViewId.apply {
                text = author.name
                setOnClickListener {
                    onAuthorClickListener.onAuthor(author)
                }
            }
            removeAuthorBtnId.apply {
                isEnabled = author.id != currentAuthorId
                setOnClickListener {
                    findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                        onAuthorClickListener.onRemove(author)
                    }
                }
            }
        }
    }
}
