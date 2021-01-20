package com.yoji.motivation.listeners

import com.yoji.motivation.dto.Idea

interface OnIdeaClickListener {
    fun onLike(idea: Idea)
    fun onDislike(idea: Idea)
    fun onShare(idea: Idea)
    fun onDelete(idea: Idea)
    fun onEdit(idea: Idea)
    fun onLink(idea: Idea)
    fun onAuthor(idea: Idea)
}