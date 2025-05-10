package com.faigenbloom.familybudget.repositories.mappers

import com.faigenbloom.familybudget.datasources.db.entities.RepeatOptions
import com.faigenbloom.familybudget.datasources.db.entities.RepeatableOptionEntity
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailEntity
import com.faigenbloom.familybudget.datasources.firebase.models.RepeatableOptionModel
import com.faigenbloom.familybudget.datasources.firebase.models.SpendingDetailModel
const val DIVIDER: String = "||"
class SpendingRepeatableOptionsSourceMapper {

    fun forServer(entity: RepeatableOptionEntity): RepeatableOptionModel {
        return RepeatableOptionModel(
            id = entity.id,
            startDate = entity.startDate,
            endDate = entity.endDate,
            repeatType = entity.repeatType.ordinal,
            excludedIDs = entity.excludedIDs.split(DIVIDER),
        )
    }

    fun forDB(model: RepeatableOptionModel): RepeatableOptionEntity {
        return RepeatableOptionEntity(
            id = model.id,
            startDate = model.startDate,
            endDate = model.endDate,
            repeatType = RepeatOptions.entries[model.repeatType],
            excludedIDs = model.excludedIDs.joinToString(DIVIDER),
        )
    }
}
