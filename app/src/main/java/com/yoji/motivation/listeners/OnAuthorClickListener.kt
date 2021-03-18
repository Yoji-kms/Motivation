package com.yoji.motivation.listeners

import com.yoji.motivation.dto.Author

interface OnAuthorClickListener {
    fun onAuthor(author: Author)
    fun onRemove(author: Author)
}