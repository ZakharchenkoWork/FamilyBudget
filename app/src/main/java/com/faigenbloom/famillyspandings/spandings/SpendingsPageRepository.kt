package com.faigenbloom.famillyspandings.spandings

import com.faigenbloom.famillyspandings.datasources.BaseDataSource

class SpendingsPageRepository(private val dataSource: BaseDataSource) {
    suspend fun getAllSpendings(): List<SpendingData> {
        return dataSource.getAllSpendings()
    }
}
