package com.faigenbloom.familybudget.repositories.mappers

import com.faigenbloom.familybudget.datasources.db.entities.PersonEntity
import com.faigenbloom.familybudget.datasources.firebase.models.PersonModel

class PersonSourceMapper {
    fun forDB(model: PersonModel): PersonEntity {
        return PersonEntity(
            id = model.id,
            familyId = model.familyId,
            name = model.name,
            familyName = model.familyName,
            isThisUser = false,
        )
    }
}