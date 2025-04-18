package com.faigenbloom.familybudget.datasources.firebase.models

import kotlinx.serialization.Serializable

@Serializable
data class SpendingModel(
    val id: String,
    val ownerId: String,
    val name: String,
    val amount: Long,
    val date: Long,
    val categoryId: String,
    val photoUri: String?,
    val manualTotal: Boolean,
    val planned: Boolean,
    val hidden: Boolean,
    val repeatOptions: Int,
    val details: List<String>,
) {
    companion object {
        const val COLLECTION_NAME = "spendings"
    }
}
