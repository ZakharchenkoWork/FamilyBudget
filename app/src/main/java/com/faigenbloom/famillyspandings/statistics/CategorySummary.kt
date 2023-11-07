package com.faigenbloom.famillyspandings.statistics

data class CategorySummary(
    val id: String,
    val nameId: Int?,
    val iconId: Int?,
    val amount: Long,
) {
    var amountPercent: Double = 0.0
}
