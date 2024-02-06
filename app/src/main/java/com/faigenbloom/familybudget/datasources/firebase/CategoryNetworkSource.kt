package com.faigenbloom.familybudget.datasources.firebase

import com.faigenbloom.familybudget.common.throughJson
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.google.firebase.firestore.FirebaseFirestore

class CategoryNetworkSource(
    firestore: FirebaseFirestore,
    private val idSource: IdSource,
) : BaseNetworkSource(firestore) {
    suspend fun saveCategory(model: CategoryModel) {
        set(
            collectionId = idSource[ID.FAMILY],
            document = CategoryModel.COLLECTION_NAME,
            innerId = model.id,
            data = model,
        )
    }

    suspend fun loadCategories(): List<CategoryModel> {
        return getAsList(
            idSource[ID.FAMILY],
            CategoryModel.COLLECTION_NAME,
        ).map { it.throughJson() }
    }
}
