package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.db.entities.FamilyEntity
import com.faigenbloom.familybudget.datasources.db.entities.PersonEntity
import com.faigenbloom.familybudget.datasources.firebase.FamilyNetworkSource
import com.faigenbloom.familybudget.datasources.firebase.models.FamilyModel
import com.faigenbloom.familybudget.repositories.mappers.FamilySourceMapper
import com.faigenbloom.familybudget.repositories.mappers.PersonSourceMapper

class FamilyRepository(
    private val dataBaseSource: BaseDataSource,
    private val networkSource: FamilyNetworkSource,
    private val personSourceMapper: PersonSourceMapper,
    private val familySourceMapper: FamilySourceMapper,
) {
    suspend fun loadFamily(personId: String): FamilyEntity? {
        val familyId = networkSource.getFamilyId(personId)
        familyId?.let {
            networkSource.getFamily(familyId)?.let {
                dataBaseSource.saveFamily(familySourceMapper.forDB(it))
                networkSource.getPersons(familyId)?.let {
                    it.forEach {
                        it?.let {
                            dataBaseSource.saveFamilyMember(personSourceMapper.forDB(it))
                        }
                    }
                }
            }
        }

        return dataBaseSource.getFamily()
    }

    suspend fun loadFamily(): FamilyEntity {
        return dataBaseSource.getFamily() ?: FamilyEntity("", "")
    }

    suspend fun saveFamily(familyEntity: FamilyEntity) {
        networkSource.createFamily(
            FamilyModel(
                id = familyEntity.id,
                name = familyEntity.name,
                members = listOf(),

                ),
        )
        dataBaseSource.saveFamily(familyEntity)
    }

    suspend fun getFamilyMembers(): List<PersonEntity> {
        return dataBaseSource.getFamilyMembers()
    }

    suspend fun saveFamilyMember(member: PersonEntity) {
        dataBaseSource.saveFamilyMember(member)
    }

    suspend fun saveFamilyMembers(members: List<PersonEntity>) {
        val oldFamilyMembers = getFamilyMembers()
        members.forEach {
            if (oldFamilyMembers.contains(it).not()) {
                networkSource.createFamilyMember(it, ArrayList(oldFamilyMembers))
                dataBaseSource.saveFamilyMember(it)
            }
        }

        /*oldFamilyMembers.forEach {
            if (members.contains(it).not()) {
                dataBaseSource.deleteFamilyMember(it)
            }
        }*/
    }
}
