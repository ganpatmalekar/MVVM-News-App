package com.swapnil.mvvmnewsapp.db

import androidx.room.TypeConverter
import com.swapnil.mvvmnewsapp.ui.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source) : String {
        return source.name
    }

    @TypeConverter
    fun toSourceToString(name: String) : Source {
        return Source(name, name)
    }
}