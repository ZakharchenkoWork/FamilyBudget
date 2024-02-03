package com.faigenbloom.familybudget.ui.statistics

data class CategorySummaryUi(
    val id: String,
    val nameId: Int?,
    val name: String?,
    val iconId: Int?,
    val iconUri: String?,
    var barDataValue: Float = 0.0f,
    var amount: Long,
) {
    var amountPercent: Double = 0.0
}
