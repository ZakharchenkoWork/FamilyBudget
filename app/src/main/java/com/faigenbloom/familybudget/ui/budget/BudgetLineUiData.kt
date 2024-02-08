package com.faigenbloom.familybudget.ui.budget

import com.faigenbloom.familybudget.common.Identifiable

data class BudgetLineUiData(
    override val id: String,
    val name: String,
    val amount: String,
) : Identifiable
