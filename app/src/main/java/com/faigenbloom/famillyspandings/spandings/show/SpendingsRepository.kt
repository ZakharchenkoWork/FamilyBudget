package com.faigenbloom.famillyspandings.spandings.show

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity

class SpendingsRepository(private val dataSource: BaseDataSource) {
    suspend fun getSpending(id: String): SpendingEntity {
        return dataSource.getSpending(id)
    }
}
