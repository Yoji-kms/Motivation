package com.yoji.motivation.converters

import android.net.Uri
import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time

    @TypeConverter
    fun fromString(string: String): Uri = Uri.parse(string)

    @TypeConverter
    fun uriToString(uri: Uri): String = uri.toString()
}