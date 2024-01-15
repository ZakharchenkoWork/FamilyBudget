package com.faigenbloom.famillyspandings.spandings.edit

import com.faigenbloom.famillyspandings.comon.checkOrGenId
import com.faigenbloom.famillyspandings.comon.toLongMoney
import com.faigenbloom.famillyspandings.comon.toReadableMoney
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity

data class SpendingDetailListWrapper(
    val details: ArrayList<SpendingDetail> = ArrayList(),
)

data class SpendingDetail(
    val id: String,
    val name: String,
    val amount: String,
    val barcode: String = "",
) {
    fun mapToEntity(): SpendingDetailEntity {
        return SpendingDetailEntity(
            id = id.checkOrGenId(),
            name = name,
            amount = amount.toLongMoney(),
            barcode = barcode,
        )
    }

    companion object {
        fun fromEntity(entity: SpendingDetailEntity): SpendingDetail {
            return SpendingDetail(
                id = entity.id,
                name = entity.name,
                amount = entity.amount.toReadableMoney(),
                barcode = entity.barcode,
            )
        }
    }
}

