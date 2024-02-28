package com.faigenbloom.familybudget.datasources.firebase.models

import com.faigenbloom.familybudget.common.Identifiable

class BudgetLineModel(
    override val id: String,
    val repeatableId: String,
    val name: String,
    val amount: Long,
    val sortableDate: Long,
    val forMonth: Boolean,
    val forFamily: Boolean,
    val ownerId: String,
    val default: Boolean = false,
    val formula: String? = null,
) : Identifiable {
    companion object {
        const val COLLECTION_NAME = "budgets"
    }
}
