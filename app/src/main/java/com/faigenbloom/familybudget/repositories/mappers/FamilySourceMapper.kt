package com.faigenbloom.familybudget.repositories.mappers

import com.faigenbloom.familybudget.datasources.db.entities.FamilyEntity
import com.faigenbloom.familybudget.datasources.firebase.models.FamilyModel

class FamilySourceMapper {
    fun forDB(model: FamilyModel): FamilyEntity {
        return FamilyEntity(
            id = model.id,
            name = model.name,
        )
    }
}
