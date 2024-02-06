package com.faigenbloom.familybudget.ui.family

import com.faigenbloom.familybudget.common.Mapper
import com.faigenbloom.familybudget.datasources.db.entities.PersonEntity

class PersonMapper : Mapper<PersonUiData, PersonEntity>() {
    override fun forUI(entity: PersonEntity): PersonUiData {
        return PersonUiData(
            id = entity.id,
            familyId = entity.familyId,
            name = entity.name,
            isThisUser = entity.isThisUser,
            familyName = entity.familyName,
        )
    }

    override fun forDB(model: PersonUiData): PersonEntity {
        return PersonEntity(
            id = model.id,
            familyId = model.familyId,
            familyName = model.familyName,
            name = model.name,
            isThisUser = model.isThisUser,
        )
    }

    override fun copyChangingId(model: PersonUiData, id: String): PersonUiData {
        return model.copy(id = id)
    }
}
