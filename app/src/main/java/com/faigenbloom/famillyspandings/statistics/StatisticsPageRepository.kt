package com.faigenbloom.famillyspandings.statistics

import com.faigenbloom.famillyspandings.datasources.BaseDataSource

class StatisticsPageRepository(private val dataSource: BaseDataSource) {
    suspend fun getCategoriesSumaries(): List<CategorySummary> {
        return dataSource.getCategoriesSumaries()
    }
}
