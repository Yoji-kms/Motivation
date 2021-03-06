package com.yoji.motivation.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yoji.motivation.dto.Author

@Entity(tableName = "authors")
data class AuthorEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String
){
    companion object {
        fun fromAuthor(author: Author) = with(author){
            AuthorEntity(id, name)
        }
    }
}

fun AuthorEntity.toAuthor(): Author = Author(id, name)
