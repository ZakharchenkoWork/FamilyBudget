package com.faigenbloom.familybudget.domain.mappers

import com.faigenbloom.familybudget.datasources.db.entities.FamilyEntity
import com.faigenbloom.familybudget.ui.family.FamilyUiData

class FamilyMapper {
    fun forUI(entity: FamilyEntity): FamilyUiData {
        return FamilyUiData(
            id = entity.id,
            name = entity.name,
            members = listOf(),
        )
    }

    fun forDB(model: FamilyUiData): FamilyEntity {
        return FamilyEntity(
            id = model.id,
            name = model.name,
        )
    }
}
