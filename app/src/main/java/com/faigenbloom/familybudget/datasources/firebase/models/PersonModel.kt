package com.faigenbloom.familybudget.datasources.firebase.models

data class PersonModel(
    val id: String,
    val familyId: String,
    val name: String,
    val familyName: String,
    val isHidden: Boolean,
) {
    companion object {
        const val COLLECTION_NAME = "person"
    }
}
