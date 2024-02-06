package com.faigenbloom.familybudget.ui.family

import com.faigenbloom.familybudget.common.Mapper
import com.faigenbloom.familybudget.datasources.db.entities.FamilyEntity

class FamilyMapper : Mapper<FamilyUiData, FamilyEntity>() {
    override fun forUI(entity: FamilyEntity): FamilyUiData {
        return FamilyUiData(
            id = entity.id,
            name = entity.name,
            members = listOf(),
        )
    }

    override fun forDB(model: FamilyUiData): FamilyEntity {
        return FamilyEntity(
            id = model.id,
            name = model.name,
        )
    }

    override fun copyChangingId(model: FamilyUiData, id: String): FamilyUiData {
        return model.copy(id = id)
    }
}
