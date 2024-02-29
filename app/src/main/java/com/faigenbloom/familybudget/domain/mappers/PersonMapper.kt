package com.faigenbloom.familybudget.domain.mappers

import com.faigenbloom.familybudget.datasources.db.entities.PersonEntity
import com.faigenbloom.familybudget.ui.family.PersonUiData

class PersonMapper {
    fun forUI(entity: PersonEntity): PersonUiData {
        return PersonUiData(
            id = entity.id,
            familyId = entity.familyId,
            name = entity.name,
            isThisUser = entity.isThisUser,
            familyName = entity.familyName,
        )
    }

    fun forDB(model: PersonUiData): PersonEntity {
        return PersonEntity(
            id = model.id,
            familyId = model.familyId,
            familyName = model.familyName,
            name = model.name,
            isThisUser = model.isThisUser,
            isHidden = false,
        )
    }
}
