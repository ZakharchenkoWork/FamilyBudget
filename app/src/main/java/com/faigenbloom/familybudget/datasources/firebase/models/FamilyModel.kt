package com.faigenbloom.familybudget.datasources.firebase.models

data class FamilyModel(
    val id: String,
    val name: String,
    val members: List<String> = emptyList(),
    val spendings: List<String>? = emptyList(),
) {
    companion object {
        const val COLLECTION_NAME = "family"
    }
}

