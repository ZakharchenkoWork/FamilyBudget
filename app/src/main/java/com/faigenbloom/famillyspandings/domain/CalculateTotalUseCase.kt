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
            var total = 0.0
            detailsList.forEach { total += if (it.amount.isNotEmpty()) it.amount.toDouble() else 0.0 }
            (total * 100).toLong().toReadableMoney()
        } else {
            amountText
        }
    }
}
