package com.faigenbloom.familybudget.datasources.firebase.models

data class ConnectionModel(
    val id: String,
    val familyId: String,
) {
    companion object {
        const val COLLECTION_NAME = "Connections"
        const val COLUMN_ID = "person_id"
        const val COLUMN_FAMILY_ID = "family_id"
    }
}
