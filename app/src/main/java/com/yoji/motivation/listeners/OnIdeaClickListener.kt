package com.yoji.motivation.listeners

import com.yoji.motivation.dto.Idea
import com.yoji.motivation.dto.IdeaWithAuthor

interface OnIdeaClickListener {
    suspend fun onLike(idea: Idea)
    suspend fun onDislike(idea: Idea)
    fun onShare(ideaWithAuthor: IdeaWithAuthor)
    suspend fun onDelete(idea: Idea)
    fun onEdit(idea: Idea)
    fun onLink(idea: Idea)
    fun onAuthor(ideaWithAuthor: IdeaWithAuthor)
}