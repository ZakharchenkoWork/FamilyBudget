package com.faigenbloom.famillyspandings.datasources.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME}")
    fun getCategories(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg yourEntities: CategoryEntity)
}
