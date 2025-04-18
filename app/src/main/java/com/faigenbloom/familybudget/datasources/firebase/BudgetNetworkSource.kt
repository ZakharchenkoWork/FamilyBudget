package com.faigenbloom.familybudget.datasources.firebase

import com.faigenbloom.familybudget.common.throughJson
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.firebase.models.BudgetLineModel
import com.faigenbloom.familybudget.datasources.firebase.models.CategoryModel
import com.faigenbloom.familybudget.datasources.firebase.models.Wrapper
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class BudgetNetworkSource(
    firestore: FirebaseFirestore,
    private val idSource: IdSource,
) : BaseNetworkSource(firestore) {
    suspend fun saveBudgets(model: List<BudgetLineModel>) {
        client.post("/budget/save/${idSource[ID.FAMILY]}") {
            setBody(model)
        }
    }

    suspend fun loadBudgets(): List<BudgetLineModel> {
        return client.get("/budget/${idSource[ID.FAMILY]}").body<Wrapper<BudgetLineModel>>().list
    }
}
