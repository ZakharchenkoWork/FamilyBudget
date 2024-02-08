package com.faigenbloom.familybudget.ui.budget

import androidx.annotation.StringRes
import com.faigenbloom.familybudget.R

enum class BudgetLabels(@StringRes val nameId: Int) {
    PLANNED_BUDGET(R.string.budget_planned_budget),
    SPENT(R.string.budget_spent),
    PLANNED_SPENDINGS(R.string.budget_planned_spendings),
    BALANCE(R.string.budget_balance_long),
    POCKET(R.string.budget_pocket),
    SAVINGS(R.string.budget_savings);

    companion object {
        fun contains(key: String): Boolean {
            return values().find {
                it.name == key
            } != null
        }
    }
}
