package com.faigenbloom.famillyspandings.spandings.edit.detail

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity

class DetailsRepository(private val dataSource: BaseDataSource) {
    suspend fun getAllDetails(): List<SpendingDetailEntity> {
        return dataSource.getAllSpendingDetails()
    }
}
