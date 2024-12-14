package com.rendox.grocerygenius.data.model

import com.rendox.grocerygenius.database.groceryicon.IconEntity
import com.rendox.grocerygenius.model.IconReference

fun IconReference.asEntity() = IconEntity(
    uniqueFileName = uniqueFileName,
    filePath = filePath
)