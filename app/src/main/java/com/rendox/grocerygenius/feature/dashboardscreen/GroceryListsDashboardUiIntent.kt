package com.rendox.grocerygenius.feature.dashboardscreen

import com.rendox.grocerygenius.model.GroceryList

sealed interface GroceryListsDashboardUiIntent {
    data class OnUpdateGroceryLists(val groceryLists: List<GroceryList>) :
        GroceryListsDashboardUiIntent
    data object OnAdderItemClick : GroceryListsDashboardUiIntent
}