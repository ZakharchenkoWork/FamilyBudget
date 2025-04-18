package com.faigenbloom.familybudget.datasources.firebase.models

import com.faigenbloom.familybudget.common.Identifiable
import kotlinx.serialization.Serializable

@Serializable
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
