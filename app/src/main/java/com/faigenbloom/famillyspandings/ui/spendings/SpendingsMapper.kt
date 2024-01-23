package com.faigenbloom.famillyspandings.ui.spendings

import androidx.core.net.toUri
import com.faigenbloom.famillyspandings.common.Mapper
import com.faigenbloom.famillyspandings.common.toLongDate
import com.faigenbloom.famillyspandings.common.toLongMoney
import com.faigenbloom.famillyspandings.common.toReadableDate
import com.faigenbloom.famillyspandings.common.toReadableMoney
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity

class SpendingsMapper : Mapper<SpendingUiData, SpendingEntity>() {
    override fun forUI(entity: SpendingEntity): SpendingUiData {
        return SpendingUiData(
            id = entity.id,
            name = entity.name,
            amount = entity.amount.toReadableMoney(),
            date = entity.date.toReadableDate(),
            categoryId = entity.categoryId,
            photoUri = entity.photoUri?.toUri(),
            isManualTotal = entity.isManualTotal,
            isPlanned = entity.isPlanned,
            isHidden = entity.isHidden,
            isDuplicate = entity.isDuplicate,
        )
    }

    override fun forDB(model: SpendingUiData): SpendingEntity {
        val date = model.date.toLongDate()
        return SpendingEntity(
            id = model.id,
            name = model.name,
            amount = model.amount.toLongMoney(),
            date = date,
            categoryId = model.categoryId,
            photoUri = model.photoUri?.toString(),
            isManualTotal = model.isManualTotal,
            isPlanned = model.isPlanned,
            isHidden = model.isHidden,
            isDuplicate = model.isDuplicate,
        )
    }

    override fun copyChangingId(model: SpendingUiData, id: String): SpendingUiData {
        return model.copy(id = id)
    }
}
