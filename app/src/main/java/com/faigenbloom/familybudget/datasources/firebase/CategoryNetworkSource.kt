package com.faigenbloom.familybudget.datasources.firebase

import com.faigenbloom.familybudget.common.throughJson
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.firebase.models.CategoryModel
import com.faigenbloom.familybudget.datasources.firebase.models.Wrapper
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class CategoryNetworkSource(
    firestore: FirebaseFirestore,
    private val idSource: IdSource,
) : BaseNetworkSource(firestore) {
    suspend fun saveCategory(model: CategoryModel) {
        client.post("/categories/save/${idSource[ID.FAMILY]}") {
            setBody(model)
        }
    }

    suspend fun loadCategories(): List<CategoryModel> {
        return client.get("/categories/${idSource[ID.FAMILY]}").body<Wrapper<CategoryModel>>().list
    }
}
