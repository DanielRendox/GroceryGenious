package com.rendox.grocerygenius.data.model

import com.rendox.grocerygenius.database.grocerylist.GroceryListEntity
import com.rendox.grocerygenius.model.GroceryList

fun GroceryList.asEntity() = GroceryListEntity(
    id = id,
    name = name,
    sortingPriority = sortingPriority
)