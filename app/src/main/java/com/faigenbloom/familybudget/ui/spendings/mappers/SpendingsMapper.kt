package com.faigenbloom.familybudget.ui.spendings.mappers

import androidx.core.net.toUri
import com.faigenbloom.familybudget.common.toLongDate
import com.faigenbloom.familybudget.common.toLongMoney
import com.faigenbloom.familybudget.common.toReadableDate
import com.faigenbloom.familybudget.common.toReadableMoney
import com.faigenbloom.familybudget.datasources.db.entities.SpendingEntity
import com.faigenbloom.familybudget.ui.spendings.SpendingUiData

class SpendingsMapper {
    fun forUI(entity: SpendingEntity): SpendingUiData {
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
            ownerId = entity.ownerId,
        )
    }

    fun forDB(model: SpendingUiData): SpendingEntity {
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
            ownerId = model.ownerId,
        )
    }
}
