package com.faigenbloom.familybudget.datasources.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faigenbloom.familybudget.datasources.db.entities.SettingsEntity


@Dao
interface SettingsDao {
    @Query("SELECT * FROM ${SettingsEntity.TABLE_NAME}")
    fun getSettings(): SettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(settingsEntity: SettingsEntity)
}
