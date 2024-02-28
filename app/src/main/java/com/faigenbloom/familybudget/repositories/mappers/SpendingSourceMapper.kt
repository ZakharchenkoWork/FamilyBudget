package com.faigenbloom.familybudget.repositories.mappers

import com.faigenbloom.familybudget.datasources.db.entities.SpendingEntity
import com.faigenbloom.familybudget.datasources.firebase.models.SpendingModel


class SpendingSourceMapper {
    fun forServer(entity: SpendingEntity): SpendingModel {
        return SpendingModel(
            id = entity.id,
            name = entity.name,
            amount = entity.amount,
            ownerId = entity.ownerId,
            date = entity.date,
            categoryId = entity.categoryId,
            photoUri = entity.photoUri,
            manualTotal = entity.isManualTotal,
            planned = entity.isPlanned,
            hidden = entity.isHidden,
            details = listOf(),
        )
    }

    fun forDB(model: SpendingModel): SpendingEntity {
        return SpendingEntity(
            id = model.id,
            ownerId = model.ownerId,
            name = model.name,
            amount = model.amount,
            date = model.date,
            categoryId = model.categoryId,
            photoUri = model.photoUri,
            isManualTotal = model.manualTotal,
            isPlanned = model.planned,
            isHidden = model.hidden,
            isDuplicate = false,
        )
    }
}
