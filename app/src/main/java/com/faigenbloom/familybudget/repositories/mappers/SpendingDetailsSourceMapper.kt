package com.faigenbloom.familybudget.repositories.mappers

import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailEntity
import com.faigenbloom.familybudget.datasources.firebase.models.SpendingDetailModel

class SpendingDetailsSourceMapper {
    fun forServer(entity: SpendingDetailEntity): SpendingDetailModel {
        return SpendingDetailModel(
            id = entity.id,
            name = entity.name,
            amount = entity.amount,
            barcode = entity.barcode,
        )
    }

    fun forDB(model: SpendingDetailModel): SpendingDetailEntity {
        return SpendingDetailEntity(
            id = model.id,
            name = model.name,
            amount = model.amount,
            barcode = model.barcode,
        )
    }
}
