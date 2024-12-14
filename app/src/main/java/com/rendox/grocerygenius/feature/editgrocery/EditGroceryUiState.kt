package com.rendox.grocerygenius.feature.editgrocery

import com.rendox.grocerygenius.model.Category
import com.rendox.grocerygenius.model.Grocery

data class EditGroceryUiState(
    val editGrocery: Grocery? = null,
    val clearEditGroceryDescriptionButtonIsShown: Boolean = false,
    val groceryCategories: List<Category> = emptyList()
)