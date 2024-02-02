package com.faigenbloom.famillyspandings.datasources.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faigenbloom.famillyspandings.datasources.entities.FamilyEntity
import com.faigenbloom.famillyspandings.datasources.entities.PersonEntity

@Dao
interface FamilyDao {
    @Query("SELECT * FROM ${FamilyEntity.TABLE_NAME}")
    suspend fun getFamily(): FamilyEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFamily(familyEntity: FamilyEntity)

    @Query("SELECT * FROM ${PersonEntity.TABLE_NAME}")
    suspend fun getFamilyMembers(): List<PersonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFamilyMembers(members: List<PersonEntity>)
}
