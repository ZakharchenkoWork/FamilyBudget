package com.faigenbloom.familybudget.datasources.firebase

import com.faigenbloom.familybudget.common.throughJson
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.firebase.models.BudgetLineModel
import com.google.firebase.firestore.FirebaseFirestore

class BudgetNetworkSource(
    firestore: FirebaseFirestore,
    private val idSource: IdSource,
) : BaseNetworkSource(firestore) {
    suspend fun saveBudgets(model: List<BudgetLineModel>) {
        set(
            collectionId = idSource[ID.FAMILY],
            document = BudgetLineModel.COLLECTION_NAME,
            data = model,
        )
    }

    suspend fun loadBudgets(): List<BudgetLineModel> {
        return getAsList(
            idSource[ID.FAMILY],
            BudgetLineModel.COLLECTION_NAME,
        ).map { it.throughJson() }
    }
}
