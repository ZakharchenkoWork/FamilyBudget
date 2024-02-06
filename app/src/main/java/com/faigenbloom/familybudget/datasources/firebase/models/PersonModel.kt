package com.faigenbloom.familybudget.datasources.firebase.models

data class PersonModel(
    val id: String,
    val familyId: String,
    val name: String,
    val familyName: String,
) {
    companion object {
        const val COLLECTION_NAME = "person"
    }
}
