package com.faigenbloom.famillyspandings.datasources

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.faigenbloom.famillyspandings.datasources.dao.BudgetDao
import com.faigenbloom.famillyspandings.datasources.dao.CategoriesDao
import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.DefaultCategories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [BudgetEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
    abstract fun categoriesDao(): CategoriesDao
}

class AppDatabaseCallback(private val appDatabase: AppDatabase) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        val entities =
            DefaultCategories.values().map { defaultCategory ->
                CategoryEntity(id = defaultCategory.name, isDefault = true)
            }.toTypedArray()
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.categoriesDao().insertAll(
                *entities,
            )
        }
    }
}
