package com.rendox.grocerygenius.data.grocerylist

import com.rendox.grocerygenius.data.model.asEntity
import com.rendox.grocerygenius.database.grocerylist.GroceryListDao
import com.rendox.grocerygenius.model.GroceryList
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GroceryListRepositoryImpl @Inject constructor(
    private val groceryListDao: GroceryListDao
) : GroceryListRepository {
    override suspend fun insertGroceryList(groceryList: GroceryList) {
        groceryListDao.insertGroceryList(groceryList.asEntity())
    }

    override fun getGroceryListById(id: String): Flow<GroceryList?> {
        return groceryListDao.getGroceryListById(id)
    }

    override fun getAllGroceryLists(): Flow<List<GroceryList>> {
        return groceryListDao.getAllGroceryLists()
    }

    override suspend fun updateGroceryListName(
        listId: String,
        name: String
    ) {
        groceryListDao.updateGroceryListName(listId, name)
    }

    override suspend fun deleteGroceryListById(groceryListId: String) {
        groceryListDao.deleteGroceryListById(groceryListId)
    }

    override suspend fun upsertGroceryLists(groceryLists: List<GroceryList>) {
        groceryListDao.upsertGroceryLists(groceryLists.map { it.asEntity() })
    }

    override suspend fun updateGroceryLists(groceryLists: List<GroceryList>) {
        groceryListDao.updateGroceryLists(groceryLists.map { it.asEntity() })
    }
}