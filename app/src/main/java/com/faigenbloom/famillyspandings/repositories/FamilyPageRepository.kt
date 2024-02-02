package com.faigenbloom.famillyspandings.repositories

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.FamilyEntity
import com.faigenbloom.famillyspandings.datasources.entities.PersonEntity

class FamilyPageRepository(private val dataSource: BaseDataSource) {
    suspend fun getFamily(): FamilyEntity {
        return dataSource.getFamily() ?: FamilyEntity("", "")
    }

    suspend fun saveFamily(familyEntity: FamilyEntity) {
        dataSource.saveFamily(familyEntity)
    }

    suspend fun getFamilyMembers(): List<PersonEntity> {
        return dataSource.getFamilyMembers()
    }

    suspend fun saveFamilyMembers(members: List<PersonEntity>) {
        dataSource.saveFamilyMembers(members)
    }
}
