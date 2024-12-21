package com.rendox.grocerygenius.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LanguageNetwork(
    val languageCode: String,
    val partiallySupported: Boolean,
)
