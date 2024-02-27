package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
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
    private val idSource: IdSource,
) {
    suspend fun getFamilyName(familyId: String): String {
        return networkSource.getFamily(familyId)?.name ?: ""
    }

    suspend fun loadFamily(personId: String): FamilyEntity? {
        val familyId = networkSource.getFamilyId(personId)
        familyId?.let {
            networkSource.getFamily(familyId)?.let {
                dataBaseSource.saveFamily(familySourceMapper.forDB(it))
                updateFamilyMembers(familyId)
            }
        }

        return dataBaseSource.getFamily()
    }

    private suspend fun updateFamilyMembers(familyId: String) {
        networkSource.getFamilyMembers(familyId)?.let {
            it.forEach {
                it?.let {
                    dataBaseSource.saveFamilyMember(personSourceMapper.forDB(it))
                }
            }
        }
    }

    suspend fun loadFamily(): FamilyEntity {
        return dataBaseSource.getFamily() ?: FamilyEntity("", "")
    }

    suspend fun saveFamily(familyEntity: FamilyEntity) {
        networkSource.createFamily(
            FamilyModel(
                id = familyEntity.id,
                name = familyEntity.name,
                members = getFamilyMembers().let {
                    if (it.isEmpty() || it[0].familyId != familyEntity.id) {
                        emptyList()
                    } else {
                        it.map { it.id }
                    }
                },
            ),
        )
        dataBaseSource.saveFamily(familyEntity)
    }

    suspend fun getFamilyMembers(): List<PersonEntity> {
        return dataBaseSource.getFamilyMembers().ifEmpty {
            updateFamilyMembers(idSource[ID.FAMILY])
            dataBaseSource.getFamilyMembers()
        }
    }

    suspend fun saveFamilyMember(member: PersonEntity) {
        dataBaseSource.saveFamilyMember(member)
    }

    suspend fun saveFamilyMembers(members: List<PersonEntity>) {
        val oldFamilyMembers = getFamilyMembers()
        members.forEach {
            if (oldFamilyMembers.contains(it).not()) {
                networkSource.createFamilyMember(
                    personSourceMapper.forServer(it),
                    oldFamilyMembers.map { personSourceMapper.forServer(it) },
                )
                dataBaseSource.saveFamilyMember(it)
            }
        }
    }

    suspend fun getCurrentFamilyMember(): PersonEntity {
        val personId = idSource[ID.USER]
        return getFamilyMembers()
            .first { it.id == personId }
    }

    suspend fun deleteUserFromFamily() {
        dataBaseSource.clean()
    }

    suspend fun migrateFamilyMember(member: PersonEntity, oldFamilyId: String) {
        networkSource.updateFamilyMember(
            personSourceMapper.forServer(member.copy(familyId = oldFamilyId, isHidden = true)),
        )
        val newFamilyMembers =
            networkSource.getFamilyMembers(idSource[ID.FAMILY])?.filterNotNull() ?: emptyList()

        networkSource.createFamilyMember(
            personSourceMapper.forServer(member.copy(familyId = idSource[ID.FAMILY])),
            newFamilyMembers,
        )
    }
}

