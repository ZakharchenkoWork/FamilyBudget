package com.faigenbloom.familybudget.datasources.firebase.models

data class CategoryModel(
    val id: String,
    val hidden: Boolean,
    val name: String,
    val photoUri: String? = null,
) {
    companion object {
        const val COLLECTION_NAME = "categories"
    }
}
