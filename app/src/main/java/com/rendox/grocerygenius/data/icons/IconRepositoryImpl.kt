package com.rendox.grocerygenius.data.icons

import android.content.Context
import android.util.Log
import com.rendox.grocerygenius.data.Synchronizer
import com.rendox.grocerygenius.data.changeListSync
import com.rendox.grocerygenius.data.model.asEntity
import com.rendox.grocerygenius.database.groceryicon.IconDao
import com.rendox.grocerygenius.model.Category
import com.rendox.grocerygenius.model.IconReference
import com.rendox.grocerygenius.network.data.sources.icon.IconNetworkDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class IconRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val iconDao: IconDao,
    private val iconNetworkDataSource: IconNetworkDataSource
) : IconRepository {

    override fun getIconsGroupedByCategory(): Flow<Map<Category, List<IconReference>>> =
        iconDao.getIconsGroupedByCategory()

    override suspend fun getGroceryIconsByName(name: String): List<IconReference> = iconDao.getGroceryIconsByName(name)

    override suspend fun syncWith(synchronizer: Synchronizer) = synchronizer.changeListSync(
        prepopulateWithInitialData = {
            val icons = iconNetworkDataSource.downloadIcons()
            iconDao.insertGroceryIcons(icons.map { it.asEntity() })
        },
        versionReader = { it.iconVersion },
        changeListFetcher = { currentVersion ->
            iconNetworkDataSource.getIconChangeList(after = currentVersion)
        },
        versionUpdater = { latestVersion ->
            copy(iconVersion = latestVersion)
        },
        modelDeleter = { iconIds ->
            iconDao.deleteIcons(iconIds)
            for (fileName in iconIds) {
                val iconFile = File(appContext.filesDir, "icons/$fileName")
                try {
                    iconFile.delete()
                } catch (e: IOException) {
                    Log.w(
                        "IconRepository",
                        "Failed to delete icon: ${iconFile.absolutePath}; ${e.message}"
                    )
                }
            }
        },
        modelUpdater = { changedIds ->
            val networkIcons = iconNetworkDataSource.downloadIconsByIds(ids = changedIds)
            iconDao.upsertGroceryIcons(networkIcons.map { it.asEntity() })
        }
    )
}