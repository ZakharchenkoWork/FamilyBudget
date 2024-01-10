package com.faigenbloom.famillyspandings.datasources

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.faigenbloom.famillyspandings.datasources.dao.BudgetDao
import com.faigenbloom.famillyspandings.datasources.dao.CategoriesDao
import com.faigenbloom.famillyspandings.datasources.dao.SpendingsDao
import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.DefaultCategories
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailsCrossRef
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        BudgetEntity::class,
        CategoryEntity::class,
        SpendingEntity::class,
        SpendingDetailEntity::class,
        SpendingDetailsCrossRef::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun spendingsDao(): SpendingsDao
}

class AppDatabaseCallback(private val appDatabase: AppDatabase) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        val entities =
            DefaultCategories.values().map { defaultCategory ->
                CategoryEntity(id = defaultCategory.name, isDefault = true)
            }
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.categoriesDao().insertAll(
                entities,
            )
            appDatabase.spendingsDao().deleteDuplicates()
        }
    }
}
