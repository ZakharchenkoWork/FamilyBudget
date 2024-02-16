package com.faigenbloom.familybudget.datasources

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.faigenbloom.familybudget.datasources.dao.BudgetDao
import com.faigenbloom.familybudget.datasources.dao.CategoriesDao
import com.faigenbloom.familybudget.datasources.dao.FamilyDao
import com.faigenbloom.familybudget.datasources.dao.SettingsDao
import com.faigenbloom.familybudget.datasources.dao.SpendingsDao
import com.faigenbloom.familybudget.datasources.db.entities.BudgetEntity
import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity
import com.faigenbloom.familybudget.datasources.db.entities.CategoryEntity
import com.faigenbloom.familybudget.datasources.db.entities.DefaultCategories
import com.faigenbloom.familybudget.datasources.db.entities.FamilyEntity
import com.faigenbloom.familybudget.datasources.db.entities.PersonEntity
import com.faigenbloom.familybudget.datasources.db.entities.SettingsEntity
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailEntity
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailsCrossRef
import com.faigenbloom.familybudget.datasources.db.entities.SpendingEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        SettingsEntity::class,
        BudgetEntity::class,
        BudgetLineEntity::class,
        CategoryEntity::class,
        SpendingEntity::class,
        SpendingDetailEntity::class,
        SpendingDetailsCrossRef::class,
        FamilyEntity::class,
        PersonEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun budgetDao(): BudgetDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun spendingsDao(): SpendingsDao
    abstract fun familyDao(): FamilyDao

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
