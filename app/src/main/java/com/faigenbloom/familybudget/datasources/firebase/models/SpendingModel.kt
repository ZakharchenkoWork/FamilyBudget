package com.faigenbloom.familybudget.datasources.firebase.models

data class SpendingModel(
    val id: String,
    val ownerId: String,
    val name: String,
    val amount: Long,
    val date: Long,
    val categoryId: String,
    val photoUri: String?,
    val isManualTotal: Boolean,
    val isPlanned: Boolean,
    val isHidden: Boolean,
    val details: List<String>,
) {
    companion object {
        const val COLLECTION_NAME = "spendings"
    }
}
