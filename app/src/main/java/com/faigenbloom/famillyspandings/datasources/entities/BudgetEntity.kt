package com.faigenbloom.famillyspandings.datasources.entities

data class BudgetEntity(
    val familyTotal: Long,
    val plannedBudget: Long,
    val spent: Long,
    val plannedSpendings: Long,
)
