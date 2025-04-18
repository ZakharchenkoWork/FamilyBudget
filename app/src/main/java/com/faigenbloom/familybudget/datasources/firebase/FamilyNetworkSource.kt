package com.faigenbloom.familybudget.datasources.firebase

import android.util.Log
import com.faigenbloom.familybudget.common.throughJson
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.firebase.models.ConnectionModel
import com.faigenbloom.familybudget.datasources.firebase.models.FamilyModel
import com.faigenbloom.familybudget.datasources.firebase.models.Message
import com.faigenbloom.familybudget.datasources.firebase.models.PersonModel
import com.faigenbloom.familybudget.datasources.firebase.models.Wrapper
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

class FamilyNetworkSource(
    firestore: FirebaseFirestore,
    private val idSource: IdSource,
) : BaseNetworkSource(firestore) {

    suspend fun getFamilyId(personId: String): String? {
        return client.get("/family/getId/$personId").body<Message>().text.also { familyId ->
            if (familyId.isNotBlank()) {
                idSource[ID.USER] = personId
                idSource[ID.FAMILY] = familyId
            }
        }
    }

    suspend fun createFamily(family: FamilyModel) {
        client.post("/family/create"){
            setBody(family)
        }
    }

    suspend fun createFamilyMember(person: PersonModel, familyMembers: List<PersonModel>) {
        getFamily(person.familyId)?.let {
            createFamily(it.copy(members = (familyMembers + person).map { it.id }))
        }
        client.post("/family/addMember"){
            setBody(person)
        }
    }

    suspend fun updateFamilyMember(person: PersonModel) {
        client.post("/family/updateMember"){
            setBody(person)
        }
    }

    suspend fun getFamilyMembers(familyId: String): List<PersonModel?>? {
        return client.get("/family/members/$familyId").body<Wrapper<PersonModel>>().list
    }

    suspend fun getPerson(familyId: String, personId: String): PersonModel? {
        return client.get("/family/member/$personId").body<PersonModel>()
    }

    suspend fun getFamily(familyId: String): FamilyModel? {
        if (familyId.contains("//")) {
            return null
        }
        return client.get("/family/$familyId").body<FamilyModel?>()
    }

    suspend fun connectFamily(personId: String, familyId: String) {
    }
}
