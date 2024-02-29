package com.faigenbloom.familybudget.domain.mappers

import com.faigenbloom.familybudget.common.Mapper
import com.faigenbloom.familybudget.common.toLongMoney
import com.faigenbloom.familybudget.common.toNormalizedMoney
import com.faigenbloom.familybudget.common.toReadableMoney
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailEntity
import com.faigenbloom.familybudget.ui.spendings.DetailUiData

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
