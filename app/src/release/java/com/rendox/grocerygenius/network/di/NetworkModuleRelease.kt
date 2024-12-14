package com.rendox.grocerygenius.network.di

import com.rendox.grocerygenius.network.data.sources.CategoryNetworkDataSource
import com.rendox.grocerygenius.network.data.sources.IconNetworkDataSource
import com.rendox.grocerygenius.network.data.sources.OfflineFirstCategoryNetworkDataSource
import com.rendox.grocerygenius.network.data.sources.OfflineFirstIconNetworkDataSource
import com.rendox.grocerygenius.network.data.sources.OfflineFirstProductNetworkDataSource
import com.rendox.grocerygenius.network.data.sources.ProductNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModuleRelease {

    @Binds
    @Singleton
    abstract fun bindCategoryNetworkDataSource(
        categoryNetworkDataSource: OfflineFirstCategoryNetworkDataSource
    ): CategoryNetworkDataSource

    @Binds
    @Singleton
    abstract fun bindProductNetworkDataSource(
        productNetworkDataSource: OfflineFirstProductNetworkDataSource
    ): ProductNetworkDataSource

    @Binds
    @Singleton
    abstract fun bindIconNetworkDataSource(
        iconNetworkDataSource: OfflineFirstIconNetworkDataSource
    ): IconNetworkDataSource
}