package com.rendox.grocerygenius.data.model

import com.rendox.grocerygenius.database.product.CombinedProduct
import com.rendox.grocerygenius.database.product.ProductEntity
import com.rendox.grocerygenius.model.Category
import com.rendox.grocerygenius.model.IconReference
import com.rendox.grocerygenius.model.Product
import com.rendox.grocerygenius.network.model.ProductNetwork

fun Product.asEntity() = ProductEntity(
    id = id,
    name = name,
    categoryId = category?.id,
    iconFileName = icon?.uniqueFileName,
    isDefault = isDefault
)

fun CombinedProduct.asExternalModel() = Product(
    id = id,
    name = name,
    icon = icon,
    category = category,
    isDefault = isDefault
)

val CombinedProduct.icon
    get() = when {
        iconId != null && iconFilePath != null -> IconReference(
            uniqueFileName = iconId,
            filePath = iconFilePath,
            name = this.name
        )

        else -> null
    }

val CombinedProduct.category
    get() = when {
        categoryId != null &&
            categoryName != null &&
            categorySortingPriority != null -> Category(
            id = categoryId,
            name = categoryName,
            sortingPriority = categorySortingPriority
        )

        else -> null
    }

fun ProductNetwork.asEntity() = ProductEntity(
    id = id,
    name = name,
    categoryId = categoryId,
    iconFileName = iconId,
    isDefault = isDefault
)