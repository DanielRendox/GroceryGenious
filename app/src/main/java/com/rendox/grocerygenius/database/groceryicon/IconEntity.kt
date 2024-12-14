package com.rendox.grocerygenius.database.groceryicon

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["filePath"], unique = true)])
data class IconEntity(
    @PrimaryKey val uniqueFileName: String,
    val filePath: String
)