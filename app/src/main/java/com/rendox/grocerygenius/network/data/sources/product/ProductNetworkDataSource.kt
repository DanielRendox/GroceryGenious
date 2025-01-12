package com.rendox.grocerygenius.network.data.sources.product

import com.rendox.grocerygenius.network.model.NetworkChangeList
import com.rendox.grocerygenius.network.model.ProductNetwork

interface ProductNetworkDataSource {
    suspend fun getAllProducts(): List<ProductNetwork>
    suspend fun getProductsByIds(ids: List<String>): List<ProductNetwork>
    suspend fun getProductChangeList(after: Int): List<NetworkChangeList>
}