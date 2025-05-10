package com.faigenbloom.familybudget.datasources.firebase

import com.faigenbloom.familybudget.common.throughJson
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.firebase.models.RepeatableOptionModel
import com.faigenbloom.familybudget.datasources.firebase.models.SpendingDetailModel
import com.faigenbloom.familybudget.datasources.firebase.models.SpendingModel
import com.faigenbloom.familybudget.datasources.firebase.models.Wrapper
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class SpendingsNetworkSource(
    firestore: FirebaseFirestore,
    private val idSource: IdSource,
) : BaseNetworkSource(firestore) {
    suspend fun saveSpending(model: SpendingModel) {
        client.post("/spendings/save/${idSource[ID.FAMILY]}") {
            setBody(model)
        }
    }

    suspend fun getSpending(spendingId: String): SpendingModel? {
        return client.get("/spendings/$spendingId").body()
    }

    suspend fun saveSpendingDetails(details: List<SpendingDetailModel>) {
        client.post("/spendings/details/save") {
            setBody(Wrapper(details))
        }
    }

    suspend fun loadSpendings(): List<SpendingModel> {
        return client.get("/spendings/all/${idSource[ID.FAMILY]}").body<Wrapper<SpendingModel>>().list
    }

    suspend fun loadDetails(): List<SpendingDetailModel> {
        return client.get("/spendings/details/${idSource[ID.FAMILY]}").body<Wrapper<SpendingDetailModel>>().list
    }

    suspend fun loadRepeatableOptions(): List<RepeatableOptionModel> {
        return client.get("/spendings/repeatable/get/${idSource[ID.FAMILY]}").body<Wrapper<RepeatableOptionModel>>().list
    }
    suspend fun saveRepeatableOptions(repeatableOptionModel: RepeatableOptionModel)  {
        client.post("/spendings/repeatable/save") {
            setBody(repeatableOptionModel)
        }
    }
}
