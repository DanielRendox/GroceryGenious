package com.rendox.grocerygenius.data.model

import com.rendox.grocerygenius.model.AppLanguage
import com.rendox.grocerygenius.network.model.LanguageNetwork

fun LanguageNetwork.asExternalModel() = AppLanguage(
    languageCode = languageCode,
    partiallySupported = partiallySupported,
)