package com.faigenbloom.famillyspandings.datasources.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity.Companion.COLUMN_ID
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity.Companion.COLUMN_PHOTO
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity.Companion.TABLE_NAME

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getCategories(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg categoryEntities: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoryEntity: CategoryEntity)

    @Query("UPDATE $TABLE_NAME SET $COLUMN_PHOTO = :photoUri WHERE $COLUMN_ID = :id")
    suspend fun updatePhoto(id: String, photoUri: String)

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = :id")
    suspend fun getCategory(id: String): CategoryEntity
}
