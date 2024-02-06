package com.faigenbloom.familybudget.datasources.firebase

import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.firebase.models.SpendingDetailModel
import com.faigenbloom.familybudget.datasources.firebase.models.SpendingModel

class NetworkDataSource(
    private val familyNetworkSource: FamilyNetworkSource,
    private val spendingsNetworkSource: SpendingsNetworkSource,
    private val categoryNetworkSource: CategoryNetworkSource,
    private val idSource: IdSource,
) {
    suspend fun saveSpending(entity: SpendingModel) {
        spendingsNetworkSource.saveSpending(entity)
        familyNetworkSource.getFamily(idSource[ID.FAMILY])?.let { family ->
            val oldSpendings = family.spendings ?: emptyList()
            familyNetworkSource.createFamily(
                family.copy(
                    spendings = ArrayList(oldSpendings).apply { add(entity.id) },
                ),
            )
        }
    }

    suspend fun saveDetails(spendingId: String, details: List<SpendingDetailModel>) {
        spendingsNetworkSource.getSpending(spendingId)?.let { spending ->
            spendingsNetworkSource.saveSpending(spending.copy(details = details.map { it.id }))
            spendingsNetworkSource.saveSpendingDetails(details)
        }
    }

    suspend fun loadSpendings(): List<SpendingModel> {
        return spendingsNetworkSource.loadSpendings()
    }

    suspend fun loadDetails(): List<SpendingDetailModel> {
        return spendingsNetworkSource.loadDetails()
    }

    suspend fun loadCategories(): List<CategoryModel> {
        return categoryNetworkSource.loadCategories()
    }

    suspend fun saveCategory(categoryModel: CategoryModel) {
        categoryNetworkSource.saveCategory(categoryModel)
    }
}

data class CategoryModel(
    val id: String,
    val isHidden: Boolean,
    val name: String,
    val photoUri: String? = null,
) {
    companion object {
        const val COLLECTION_NAME = "categories"
    }
}

