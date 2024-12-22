package com.rendox.grocerygenius.data.language

import com.rendox.grocerygenius.data.model.asExternalModel
import com.rendox.grocerygenius.filestorage.JsonAssetDecoder
import com.rendox.grocerygenius.model.AppLanguage
import com.rendox.grocerygenius.network.data.sources.language.LanguageNetworkDataSource
import com.rendox.grocerygenius.network.listAdapter
import com.rendox.grocerygenius.network.model.LanguageNetwork
import com.squareup.moshi.Moshi
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

class LanguageRepositoryImpl @Inject constructor(
    private val languageNetworkDataSource: LanguageNetworkDataSource,
    private val moshi: Moshi,
    private val jsonAssetDecoder: JsonAssetDecoder
) : LanguageRepository {
    override suspend fun getSupportedLanguages(): List<AppLanguage> {
        return try {
            languageNetworkDataSource.getSupportedLanguages().map { it.asExternalModel() }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            jsonAssetDecoder.decodeFromFile(
                adapter = moshi.listAdapter<LanguageNetwork>(),
                fileName = "languages_supported_locally.json"
            )?.map { it.asExternalModel() } ?: emptyList()
        }
    }
}