package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.entities.FamilyEntity
import com.faigenbloom.familybudget.datasources.entities.PersonEntity
import com.faigenbloom.familybudget.datasources.firebase.FamilyModel
import com.faigenbloom.familybudget.datasources.firebase.FirestoreNetworkSource

class FamilyRepository(
    private val dataBaseSource: BaseDataSource,
    private val networkSource: FirestoreNetworkSource,
) {
    suspend fun getFamily(personId: String): FamilyEntity {
        val familyId = networkSource.getFamilyId(personId)
        networkSource.getFamily(familyId)?.let {
            dataBaseSource.saveFamily(FamilyEntity(it.id, it.name))
            networkSource.getPersons(familyId)?.let {
                it.forEach {
                    it?.let {
                        dataBaseSource.saveFamilyMember(it)
                    }
                }
            }
        }
        return dataBaseSource.getFamily() ?: FamilyEntity("", "")
    }

    suspend fun getFamily(): FamilyEntity {
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
