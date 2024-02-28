package com.faigenbloom.familybudget.datasources.firebase

import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.firebase.models.BudgetLineModel
import com.faigenbloom.familybudget.datasources.firebase.models.CategoryModel
import com.faigenbloom.familybudget.datasources.firebase.models.SpendingDetailModel
import com.faigenbloom.familybudget.datasources.firebase.models.SpendingModel

class NetworkDataSource(
    private val familyNetworkSource: FamilyNetworkSource,
    private val spendingsNetworkSource: SpendingsNetworkSource,
    private val categoryNetworkSource: CategoryNetworkSource,
    private val budgetNetworkSource: BudgetNetworkSource,
    private val imageSource: ImageSource,
    private val idSource: IdSource,
) {
    suspend fun saveSpending(model: SpendingModel) {
        spendingsNetworkSource.saveSpending(model)
        model.photoUri?.let {
            imageSource.upload(it)
        }

        familyNetworkSource.getFamily(idSource[ID.FAMILY])?.let { family ->
            val oldSpendings = family.spendings ?: emptyList()
            familyNetworkSource.createFamily(
                family.copy(
                    spendings = ArrayList(oldSpendings).apply { add(model.id) },
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
        val loadSpendings = spendingsNetworkSource.loadSpendings()
        loadSpendings.forEach {
            it.photoUri?.let { uri ->
                imageSource.download(uri)
            }
        }
        return loadSpendings
    }

    suspend fun loadDetails(): List<SpendingDetailModel> {
        return spendingsNetworkSource.loadDetails()
    }

    suspend fun loadCategories(): List<CategoryModel> {
        val loadCategories = categoryNetworkSource.loadCategories()
        loadCategories.forEach {
            it.photoUri?.let { uri ->
                imageSource.download(uri)
            }
        }
        return loadCategories
    }

    suspend fun saveCategory(categoryModel: CategoryModel) {
        categoryNetworkSource.saveCategory(categoryModel)
        categoryModel.photoUri?.let {
            imageSource.upload(it)
        }
    }

    suspend fun saveBudgetLines(budgetLines: List<BudgetLineModel>) {
        budgetNetworkSource.saveBudgets(budgetLines)
    }

    suspend fun loadBudgets(): List<BudgetLineModel> {
        return budgetNetworkSource.loadBudgets().filter {
            it.forFamily || it.ownerId == idSource[ID.USER]
        }
    }
}



