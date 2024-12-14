package com.rendox.grocerygenius.network.data.sources

import com.rendox.grocerygenius.filestorage.JsonAssetDecoder
import com.rendox.grocerygenius.network.listAdapter
import com.rendox.grocerygenius.network.model.NetworkChangeList
import com.rendox.grocerygenius.network.model.ProductNetwork
import com.squareup.moshi.Moshi
import javax.inject.Inject

class FakeProductNetworkDataSource @Inject constructor(
    private val jsonAssetDecoder: JsonAssetDecoder,
    private val moshi: Moshi
) : ProductNetworkDataSource {

    override suspend fun getAllProducts(): List<ProductNetwork> = jsonAssetDecoder.decodeFromFile(
        adapter = moshi.listAdapter<ProductNetwork>(),
        fileName = "product/default_products_en.json"
    ) ?: emptyList()

    override suspend fun getProductsByIds(ids: List<String>): List<ProductNetwork> =
        getAllProducts().filter { it.id in ids }

    override suspend fun getProductChangeList(after: Int): List<NetworkChangeList> = jsonAssetDecoder.decodeFromFile(
        adapter = moshi.listAdapter<NetworkChangeList>(),
        fileName = "product/default_products_change_list.json"
    )?.filter { it.changeListVersion > after } ?: emptyList()
}