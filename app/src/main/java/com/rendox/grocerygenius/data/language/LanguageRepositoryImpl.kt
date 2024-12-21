package com.rendox.grocerygenius.data.language

import com.rendox.grocerygenius.data.model.asExternalModel
import com.rendox.grocerygenius.model.AppLanguage
import com.rendox.grocerygenius.network.data.sources.language.LanguageNetworkDataSource
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor(
    private val languageNetworkDataSource: LanguageNetworkDataSource
): LanguageRepository {
    override suspend fun getSupportedLanguages(): List<AppLanguage> {
        return languageNetworkDataSource.getSupportedLanguages().map { it.asExternalModel() }
    }
}