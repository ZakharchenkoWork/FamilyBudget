package com.faigenbloom.famillyspandings.ui.statistics

data class CategorySummary(
    val id: String,
    val nameId: Int?,
    val name: String?,
    val iconId: Int?,
    val iconUri: String?,
    var amount: Long,
) {
    var amountPercent: Double = 0.0
}
