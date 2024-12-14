package com.rendox.grocerygenius.network.data.sources

import com.rendox.grocerygenius.network.model.CategoryNetwork
import com.rendox.grocerygenius.network.model.NetworkChangeList

interface CategoryNetworkDataSource {
    suspend fun getAllCategories(): List<CategoryNetwork>
    suspend fun getCategoriesByIds(ids: List<String>): List<CategoryNetwork>
    suspend fun getCategoryChangeList(after: Int): List<NetworkChangeList>
}