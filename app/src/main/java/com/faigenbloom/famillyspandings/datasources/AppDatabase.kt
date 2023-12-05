package com.faigenbloom.famillyspandings.datasources

import androidx.room.Database
import androidx.room.RoomDatabase
import com.faigenbloom.famillyspandings.datasources.dao.BudgetDao
import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity

@Database(entities = [BudgetEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
}
