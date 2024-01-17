package com.faigenbloom.famillyspandings.ui.spandings

import com.faigenbloom.famillyspandings.comon.Identifiable
import com.faigenbloom.famillyspandings.comon.checkOrGenId
import com.faigenbloom.famillyspandings.comon.toLongMoney
import com.faigenbloom.famillyspandings.comon.toReadableMoney
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity

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

