package com.faigenbloom.famillyspandings.domain

import com.faigenbloom.famillyspandings.comon.toReadableMoney
import com.faigenbloom.famillyspandings.ui.spandings.DetailUiData

class CalculateTotalUseCase {
    operator fun invoke(
        isManualTotal: Boolean,
        detailsList: List<DetailUiData>,
        amountText: String,
    ): String {
        return if (!isManualTotal) {
            val total = detailsList
                .sumOf {
                    if (it.amount.isNotBlank()) it.amount.toDouble() else 0.0
                }
            (total * 100).toLong().toReadableMoney()
        } else {
            amountText
        }
    }
}
