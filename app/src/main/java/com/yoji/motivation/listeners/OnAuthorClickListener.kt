package com.yoji.motivation.listeners

import com.yoji.motivation.dto.Author

interface OnAuthorClickListener {
    fun onAuthor(author: Author)
    suspend fun onRemove(author: Author)
}