package com.rendox.grocerygenius.data.grocery

import com.rendox.grocerygenius.model.Grocery
import java.util.UUID
import kotlinx.coroutines.flow.Flow

interface GroceryRepository {

    suspend fun addGroceryToList(
        productId: String,
        listId: String,
        description: String? = null,
        purchased: Boolean,
        purchasedLastModified: Long = System.currentTimeMillis()
    )

    suspend fun insertProductAndGrocery(
        name: String,
        productId: String = UUID.randomUUID().toString(),
        iconId: String? = null,
        categoryId: String?,
        groceryListId: String,
        description: String?,
        purchased: Boolean = false,
        purchasedLastModified: Long = System.currentTimeMillis(),
        isDefault: Boolean = false
    )

    fun getGroceriesFromList(listId: String): Flow<List<Grocery>>
    fun getGroceryById(
        productId: String,
        listId: String
    ): Flow<Grocery?>

    suspend fun updatePurchased(
        productId: String,
        listId: String,
        purchased: Boolean,
        purchasedLastModified: Long = System.currentTimeMillis()
    )

    suspend fun updateDescription(
        productId: String,
        listId: String,
        description: String?
    )
    suspend fun removeGroceryFromList(
        productId: String,
        listId: String
    )
}