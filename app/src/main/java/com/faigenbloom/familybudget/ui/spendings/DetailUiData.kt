package com.faigenbloom.familybudget.ui.spendings

import com.faigenbloom.familybudget.common.Identifiable
import com.faigenbloom.familybudget.common.checkOrGenId
import com.faigenbloom.familybudget.common.toLongMoney
import com.faigenbloom.familybudget.common.toReadableMoney
import com.faigenbloom.familybudget.datasources.entities.SpendingDetailEntity

data class SpendingDetailListWrapper(
    val details: ArrayList<DetailUiData> = ArrayList(),
)

data class DetailUiData(
    override val id: String,
    val name: String,
    val amount: String,
    val barcode: String = "",
) : Identifiable {
    fun mapToEntity(): SpendingDetailEntity {
        return SpendingDetailEntity(
            id = id.checkOrGenId(),
            name = name,
            amount = amount.toLongMoney(),
            barcode = barcode,
        )
    }

    companion object {
        fun fromEntity(entity: SpendingDetailEntity): DetailUiData {
            return DetailUiData(
                id = entity.id,
                name = entity.name,
                amount = entity.amount.toReadableMoney(),
                barcode = entity.barcode,
            )
        }
    }
}

