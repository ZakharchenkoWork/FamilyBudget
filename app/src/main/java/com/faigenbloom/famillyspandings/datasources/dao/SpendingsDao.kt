package com.faigenbloom.famillyspandings.datasources.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailsCrossRef
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity

@Dao
interface SpendingsDao {
    companion object {
        const val TRUE = true
        const val FALSE = false
    }

    @Query("SELECT * FROM ${SpendingEntity.TABLE_NAME} WHERE ${SpendingEntity.COLUMN_IS_PLANNED} = :isPlanned AND ${SpendingEntity.COLUMN_IS_DUPLICATE} = $FALSE")
    suspend fun getSpendings(isPlanned: Boolean): List<SpendingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSpending(entity: SpendingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSpendingDetails(entity: List<SpendingDetailEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSpendingsCrossRef(join: List<SpendingDetailsCrossRef>)

    @Query("SELECT * FROM ${SpendingDetailsCrossRef.TABLE_NAME} WHERE ${SpendingEntity.COLUMN_ID} = :id")
    suspend fun getSpendingsCrossRef(id: String): List<SpendingDetailsCrossRef>

    @Query("SELECT * FROM ${SpendingEntity.TABLE_NAME} WHERE ${SpendingEntity.COLUMN_ID} = :id")
    suspend fun getSpending(id: String): SpendingEntity

    @Query("SELECT * FROM ${SpendingDetailEntity.TABLE_NAME} WHERE ${SpendingDetailEntity.COLUMN_ID} IN (:iDs)")
    suspend fun getSpendingDetails(iDs: List<String>): List<SpendingDetailEntity>

    @Query("UPDATE ${SpendingEntity.TABLE_NAME} SET ${SpendingEntity.COLUMN_IS_PLANNED} = :isPlanned WHERE ${SpendingEntity.COLUMN_ID} = :spendingId")
    suspend fun updatePlanned(spendingId: String, isPlanned: Boolean)

    @Query("DELETE FROM ${SpendingEntity.TABLE_NAME} WHERE ${SpendingEntity.COLUMN_IS_DUPLICATE} = $TRUE")
    suspend fun deleteDuplicates()
}
