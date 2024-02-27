package com.faigenbloom.familybudget.datasources.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faigenbloom.familybudget.datasources.db.entities.FamilyEntity
import com.faigenbloom.familybudget.datasources.db.entities.PersonEntity

@Dao
interface FamilyDao {
    @Query("SELECT * FROM ${FamilyEntity.TABLE_NAME}")
    suspend fun getFamily(): FamilyEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFamily(familyEntity: FamilyEntity)

    @Query("SELECT * FROM ${PersonEntity.TABLE_NAME}")
    suspend fun getFamilyMembers(): List<PersonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFamilyMember(member: PersonEntity)

    @Query("DELETE FROM ${FamilyEntity.TABLE_NAME}")
    suspend fun deleteFamily()

    @Query("DELETE FROM ${PersonEntity.TABLE_NAME}")
    suspend fun deleteMembers()
}
