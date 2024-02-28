package com.faigenbloom.familybudget.repositories

import com.faigenbloom.familybudget.datasources.BaseDataSource
import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity
import com.faigenbloom.familybudget.datasources.firebase.NetworkDataSource
import com.faigenbloom.familybudget.repositories.mappers.BudgetLineSourceMapper

class BudgetRepository(
    private val dataSource: BaseDataSource,
    private val networkDataSource: NetworkDataSource,
    private val budgetLineSourceMapper: BudgetLineSourceMapper,
    private val idSource: IdSource,
) {
    suspend fun getBudgetLines(
        isForMonth: Boolean,
        isForFamily: Boolean,
        from: Long,
        to: Long,
    ): List<BudgetLineEntity> {
        return dataSource.getBudgetLines(
            isForMonth,
            isForFamily,
            from,
            to,
        )
    }

    suspend fun saveBudgetLines(budgetEntities: List<BudgetLineEntity>) {
        dataSource.saveBudgetLines(budgetEntities)
        networkDataSource.saveBudgetLines(
            budgetEntities.map {
                budgetLineSourceMapper.forServer(
                    it,
                    idSource[ID.USER],
                )
            },
        )
    }

    suspend fun loadBudgetLines() {
        val budgetEntities =
            networkDataSource.loadBudgets().map { budgetLineSourceMapper.forDB(it) }
        dataSource.saveBudgetLines(budgetEntities)
    }
}

