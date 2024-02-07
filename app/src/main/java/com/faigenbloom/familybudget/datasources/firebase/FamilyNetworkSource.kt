package com.faigenbloom.familybudget.datasources.firebase

import com.faigenbloom.familybudget.common.throughJson
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.db.entities.PersonEntity
import com.faigenbloom.familybudget.datasources.firebase.models.ConnectionModel
import com.faigenbloom.familybudget.datasources.firebase.models.FamilyModel
import com.faigenbloom.familybudget.datasources.firebase.models.PersonModel
import com.google.firebase.firestore.FirebaseFirestore

class FamilyNetworkSource(
    firestore: FirebaseFirestore,
    private val idSource: IdSource,
) : BaseNetworkSource(firestore) {

    suspend fun getFamilyId(personId: String): String? {
        idSource[ID.USER] = personId
        val familyId = get(ConnectionModel.COLLECTION_NAME, personId)
            ?.throughJson<ConnectionModel>()
            ?.familyId
        familyId?.let { idSource[ID.FAMILY] = it }
        return familyId
    }

    suspend fun createFamily(family: FamilyModel) {
        set(
            collectionId = family.id,
            document = FamilyModel.COLLECTION_NAME,
            data = family,
        )
    }

    suspend fun createFamilyMember(person: PersonEntity, familyMembers: ArrayList<PersonEntity>) {
        connectFamily(person.id, person.familyId)
        familyMembers.add(person)
        getFamily(person.familyId)?.let {
            createFamily(it.copy(members = familyMembers.map { it.id }))
        }
        set(
            collectionId = person.familyId,
            document = PersonModel.COLLECTION_NAME,
            innerId = person.id,
            data = person,
        )
    }

    suspend fun getPersons(familyId: String): List<PersonModel?>? {
        val family = getFamily(familyId)
        return family?.members?.map {
            getPerson(familyId, it)
        }
    }

    suspend fun getPerson(familyId: String, personId: String): PersonModel? {
        return get(
            collectionId = familyId,
            document = PersonModel.COLLECTION_NAME,
            id = personId,
        )?.throughJson()
    }

    suspend fun getFamily(familyId: String): FamilyModel? {
        return get(
            familyId,
            FamilyModel.COLLECTION_NAME,
        )?.throughJson()
    }

    suspend fun connectFamily(personId: String, familyId: String) {
        set(
            collectionId = ConnectionModel.COLLECTION_NAME,
            document = personId,
            data = ConnectionModel(personId, familyId),
        )
    }
}
