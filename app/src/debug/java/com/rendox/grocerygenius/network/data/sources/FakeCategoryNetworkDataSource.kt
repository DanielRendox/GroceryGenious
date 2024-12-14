package com.rendox.grocerygenius.network.data.sources

import com.rendox.grocerygenius.filestorage.JsonAssetDecoder
import com.rendox.grocerygenius.network.listAdapter
import com.rendox.grocerygenius.network.model.CategoryNetwork
import com.rendox.grocerygenius.network.model.NetworkChangeList
import com.squareup.moshi.Moshi
import javax.inject.Inject

class FakeCategoryNetworkDataSource @Inject constructor(
    private val jsonAssetDecoder: JsonAssetDecoder,
    private val moshi: Moshi
) : CategoryNetworkDataSource {
    override suspend fun getAllCategories(): List<CategoryNetwork> = jsonAssetDecoder.decodeFromFile(
        adapter = moshi.listAdapter<CategoryNetwork>(),
        fileName = "category/categories_en.json"
    ) ?: emptyList()

    override suspend fun getCategoriesByIds(ids: List<String>): List<CategoryNetwork> = getAllCategories().filter {
        it.id in ids
    }

    override suspend fun getCategoryChangeList(after: Int): List<NetworkChangeList> = jsonAssetDecoder.decodeFromFile(
        adapter = moshi.listAdapter<NetworkChangeList>(),
        fileName = "category/categories_change_list.json"
    )?.filter { it.changeListVersion > after } ?: emptyList()
}