package com.faigenbloom.familybudget.domain.mappers

import com.faigenbloom.familybudget.common.toLongDate
import com.faigenbloom.familybudget.common.toReadableDate
import com.faigenbloom.familybudget.datasources.db.entities.RepeatOptions
import com.faigenbloom.familybudget.datasources.db.entities.RepeatableOptionEntity
import com.faigenbloom.familybudget.repositories.mappers.DIVIDER
import com.faigenbloom.familybudget.ui.spendings.RepeatOptionsUi
import com.faigenbloom.familybudget.ui.spendings.RepeatableOptionDataUi

const val DIVIDER: String = "||"

class RepeatableOptionsMapper {
    fun forUI(entity: RepeatableOptionEntity): RepeatableOptionDataUi {
        return RepeatableOptionDataUi(
            id = entity.id,
            startDate = entity.startDate.toReadableDate(),
            endDate = entity.endDate.toReadableDate(),
            repeatType = RepeatOptionsUi.entries[entity.repeatType.ordinal],
            excludedIDs = entity.excludedIDs.split(DIVIDER),
        )
    }

    fun forDB(model: RepeatableOptionDataUi): RepeatableOptionEntity {
        return RepeatableOptionEntity(
            id = model.id,
            startDate = model.startDate.toLongDate(),
            endDate = model.endDate.toLongDate(),
            repeatType = RepeatOptions.entries[model.repeatType.ordinal],
            excludedIDs = model.excludedIDs.joinToString(DIVIDER),
        )
    }
}

