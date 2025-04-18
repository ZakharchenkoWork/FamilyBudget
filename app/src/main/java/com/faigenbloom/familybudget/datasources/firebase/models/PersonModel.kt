package com.faigenbloom.familybudget.datasources.firebase.models

import kotlinx.serialization.Serializable

@Serializable
data class PersonModel(
    val id: String,
    val familyId: String,
    val name: String,
    val familyName: String,
    val hidden: Boolean,
) {
    companion object {
        const val COLLECTION_NAME = "person"
    }
}
