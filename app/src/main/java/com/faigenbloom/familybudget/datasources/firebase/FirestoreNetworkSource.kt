package com.faigenbloom.familybudget.datasources.firebase

import android.util.Log
import com.faigenbloom.familybudget.common.TAG
import com.faigenbloom.familybudget.common.executeSuspendable
import com.faigenbloom.familybudget.common.fromJson
import com.faigenbloom.familybudget.common.toJson
import com.faigenbloom.familybudget.datasources.entities.PersonEntity
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreNetworkSource(
    private val firestore: FirebaseFirestore,
) {

    suspend fun getFamilyId(personId: String): String {
        return executeSuspendable { callback ->
            firestore.collection("Connections")
                .document(personId)
                .get()
                .addOnSuccessListener { result ->
                    callback(result?.data?.get(PersonEntity.COLUMN_FAMILY_ID)?.toString() ?: "")
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG(), "Error getting documents.", exception)
                }
        }
    }

    suspend fun createFamily(family: FamilyModel) {
        executeSuspendable { callback ->
            firestore.collection(family.id)
                .document(FamilyModel.TABLE_NAME)
                .set(family)
                .addOnSuccessListener { documentReference ->
                    callback(documentReference)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG(), "Error getting documents.", exception)
                }
        }
    }

    suspend fun createFamilyMember(person: PersonEntity, familyMembers: ArrayList<PersonEntity>) {
        connectFamily(person.id, person.familyId)
        familyMembers.add(person)
        getFamily(person.familyId)?.let {
            createFamily(it.copy(members = familyMembers.map { it.id }))
        }
        executeSuspendable { callback ->
            firestore.collection(person.familyId)
                .document(PersonEntity.TABLE_NAME)
                .collection(person.id)
                .document(person.id)
                .set(person)
                .addOnSuccessListener { documentReference ->
                    callback(documentReference)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG(), "Error getting documents.", exception)
                }
        }
    }

    suspend fun getPersons(familyId: String): List<PersonEntity?>? {
        val family = getFamily(familyId)
        return family?.members?.map {
            getPerson(familyId, it)
        }
    }

    suspend fun getPerson(familyId: String, personId: String): PersonEntity? {
        return executeSuspendable { callback ->
            firestore.collection(familyId)
                .document(PersonEntity.TABLE_NAME)
                .collection(personId)
                .document(personId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        callback(document.data?.toJson()?.fromJson())
                        Log.d(TAG(), "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d(TAG(), "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG(), "get failed with ", exception)
                }
        }
    }

    suspend fun getFamily(familyId: String): FamilyModel? {
        return executeSuspendable { callback ->
            firestore.collection(familyId)
                .document(FamilyModel.TABLE_NAME)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        callback(document.data?.toJson()?.fromJson())
                        Log.d(TAG(), "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d(TAG(), "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG(), "get failed with ", exception)
                }
        }
    }

    suspend fun connectFamily(personId: String, familyId: String) {
        executeSuspendable { callback ->
            firestore.collection("Connections")
                .document(personId)
                .set(hashMapOf(PersonEntity.COLUMN_FAMILY_ID to familyId))
                .addOnSuccessListener { documentReference ->
                    callback(documentReference)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG(), "Error getting documents.", exception)
                }
        }
    }

}

data class FamilyModel(
    val id: String,
    val name: String,
    val members: List<String> = emptyList(),
) {
    companion object {
        const val TABLE_NAME = "family"
        const val COLUMN_FAMILY_ID = "family_id"
        const val COLUMN_FAMILY_NAME = "familyName"
    }
}
