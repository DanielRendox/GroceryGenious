package com.rendox.grocerygenius.network.data.sources.language

import com.rendox.grocerygenius.network.GitHubApi
import com.rendox.grocerygenius.network.model.LanguageNetwork
import javax.inject.Inject

class LanguageNetworkDataSource @Inject constructor(
    private val gitHubApi: GitHubApi
) {
    suspend fun getSupportedLanguages(): List<LanguageNetwork> = gitHubApi.getSupportedLanguages()
}