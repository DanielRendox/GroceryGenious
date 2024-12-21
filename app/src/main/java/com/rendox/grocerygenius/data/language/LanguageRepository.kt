package com.rendox.grocerygenius.data.language

import com.rendox.grocerygenius.model.AppLanguage

interface LanguageRepository {
    suspend fun getSupportedLanguages(): List<AppLanguage>
}