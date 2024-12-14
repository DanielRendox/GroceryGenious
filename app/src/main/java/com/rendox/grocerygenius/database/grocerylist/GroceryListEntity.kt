package com.rendox.grocerygenius.database.grocerylist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroceryListEntity(
    @PrimaryKey val id: String,
    val name: String,
    val sortingPriority: Long
)