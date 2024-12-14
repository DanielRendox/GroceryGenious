package com.rendox.grocerygenius.feature.iconpicker

import com.rendox.grocerygenius.model.IconReference

sealed interface IconPickerIntent {
    data class OnPickIcon(val iconReference: IconReference) : IconPickerIntent
    data class OnUpdateSearchQuery(val query: String) : IconPickerIntent
    data object OnClearSearchQuery : IconPickerIntent
    data object OnRemoveIcon : IconPickerIntent
}