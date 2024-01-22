package com.faigenbloom.famillyspandings.ui.spandings

import com.faigenbloom.famillyspandings.comon.Mapper
import com.faigenbloom.famillyspandings.comon.toLongMoney
import com.faigenbloom.famillyspandings.comon.toNormalizedMoney
import com.faigenbloom.famillyspandings.comon.toReadableMoney
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity

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
