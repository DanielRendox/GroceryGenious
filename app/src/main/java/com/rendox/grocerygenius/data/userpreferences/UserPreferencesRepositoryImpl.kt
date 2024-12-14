package com.rendox.grocerygenius.data.userpreferences

import com.rendox.grocerygenius.data.grocerylist.GroceryListRepository
import com.rendox.grocerygenius.datastore.UserPreferencesDataSource
import com.rendox.grocerygenius.model.DarkThemeConfig
import com.rendox.grocerygenius.model.GroceryGeniusColorScheme
import com.rendox.grocerygenius.model.UserPreferences
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val groceryListRepository: GroceryListRepository
) : UserPreferencesRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val userPreferencesFlow: Flow<UserPreferences>
        get() = userPreferencesDataSource.userPreferencesFlow
            .flatMapLatest { userPreferences ->
                if (userPreferences.defaultListId != null) {
                    groceryListRepository
                        .getGroceryListById(userPreferences.defaultListId)
                        .map { groceryList ->
                            userPreferences.copy(defaultListId = groceryList?.id)
                        }
                } else {
                    flowOf(userPreferences)
                }
            }

    override suspend fun updateDefaultListId(listId: String?) {
        userPreferencesDataSource.updateDefaultListId(listId)
    }

    override suspend fun updateLastOpenedListId(listId: String) {
        userPreferencesDataSource.updateLastOpenedListId(listId)
    }

    override suspend fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferencesDataSource.updateDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun updateUseSystemAccentColor(useSystemAccentColor: Boolean) {
        userPreferencesDataSource.updateUseSystemAccentColor(useSystemAccentColor)
    }

    override suspend fun updateOpenLastViewedList(openLastViewedList: Boolean) {
        userPreferencesDataSource.updateOpenLastViewedList(openLastViewedList)
    }

    override suspend fun updateSelectedTheme(selectedTheme: GroceryGeniusColorScheme) {
        userPreferencesDataSource.updateSelectedTheme(selectedTheme)
    }

    override suspend fun getGroceryListIdToOpenOnStartup(): String? {
        val userPreferences = userPreferencesFlow.first()
        val lastOpenedListId =
            if (userPreferences.openLastViewedList) userPreferences.lastOpenedListId else null
        val resultingListId = lastOpenedListId ?: userPreferences.defaultListId
        return resultingListId?.let { listId ->
            // to ensure that the list with this id exists
            groceryListRepository.getGroceryListById(listId).first()?.id
        }
    }
}