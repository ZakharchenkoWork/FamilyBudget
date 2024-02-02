package com.faigenbloom.famillyspandings.ui.family

import com.faigenbloom.famillyspandings.common.Mapper
import com.faigenbloom.famillyspandings.datasources.entities.FamilyEntity

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
