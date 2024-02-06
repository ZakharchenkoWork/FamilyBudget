package com.faigenbloom.familybudget.datasources.firebase.models

import com.faigenbloom.familybudget.common.Identifiable

data class SpendingDetailModel(
    override val id: String,
    val name: String,
    val amount: Long,
    val barcode: String,
) : Identifiable {
    companion object {
        const val COLLECTION_NAME = "details"
    }
}
