package com.faigenbloom.famillyspandings.ui.spendings.mappers

import com.faigenbloom.famillyspandings.common.Mapper
import com.faigenbloom.famillyspandings.common.toLongMoney
import com.faigenbloom.famillyspandings.common.toNormalizedMoney
import com.faigenbloom.famillyspandings.common.toReadableMoney
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.ui.spendings.DetailUiData

class DetailsMapper : Mapper<DetailUiData, SpendingDetailEntity>() {
    override fun forUI(entity: SpendingDetailEntity): DetailUiData {
        return DetailUiData(
            id = entity.id,
            name = entity.name,
            amount = entity.amount.toReadableMoney().toNormalizedMoney(),
            barcode = entity.barcode,
        )
    }

    override fun forDB(model: DetailUiData): SpendingDetailEntity {
        return SpendingDetailEntity(
            id = model.id,
            name = model.name,
            amount = model.amount.toLongMoney(),
            barcode = model.barcode,
        )
    }

    override fun copyChangingId(model: DetailUiData, id: String): DetailUiData {
        return model.copy(id = id)
    }
}
