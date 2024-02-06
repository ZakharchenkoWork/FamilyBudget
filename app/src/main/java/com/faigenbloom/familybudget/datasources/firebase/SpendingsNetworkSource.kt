package com.faigenbloom.familybudget.datasources.firebase

import com.faigenbloom.familybudget.common.throughJson
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.firebase.models.SpendingDetailModel
import com.faigenbloom.familybudget.datasources.firebase.models.SpendingModel
import com.google.firebase.firestore.FirebaseFirestore

class SpendingsNetworkSource(
    firestore: FirebaseFirestore,
    private val idSource: IdSource,
) : BaseNetworkSource(firestore) {
    suspend fun saveSpending(model: SpendingModel) {
        set(
            collectionId = idSource[ID.FAMILY],
            document = SpendingModel.COLLECTION_NAME,
            innerId = model.id,
            data = model,
        )
    }

    suspend fun getSpending(spendingId: String): SpendingModel? {
        return get(
            collectionId = idSource[ID.FAMILY],
            document = SpendingModel.COLLECTION_NAME,
            id = spendingId,
        )?.throughJson()
    }

    suspend fun saveSpendingDetails(details: List<SpendingDetailModel>) {
        return set(
            collectionId = idSource[ID.FAMILY],
            document = SpendingDetailModel.COLLECTION_NAME,
            data = details,
        )
    }

    suspend fun loadSpendings(): List<SpendingModel> {
        return getAsList(
            idSource[ID.FAMILY],
            SpendingModel.COLLECTION_NAME,
        ).map { it.throughJson() }
    }

    suspend fun loadDetails(): List<SpendingDetailModel> {
        return getAsList(
            idSource[ID.FAMILY],
            SpendingDetailModel.COLLECTION_NAME,
        ).map { it.throughJson() }
    }
}
