package com.faigenbloom.famillyspandings.datasources.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.faigenbloom.famillyspandings.datasources.entities.DateRange
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailsCrossRef
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity

@Dao
interface SpendingsDao {
    companion object {
        const val TRUE = true
        const val FALSE = false
    }

    @Query(
        "SELECT * FROM ${SpendingEntity.TABLE_NAME} " +
                "WHERE ${SpendingEntity.COLUMN_IS_PLANNED} = :isPlanned " +
                "AND ${SpendingEntity.COLUMN_IS_DUPLICATE} = $FALSE",
    )
    suspend fun getSpendings(isPlanned: Boolean): List<SpendingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSpending(entity: SpendingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSpendingDetails(entity: List<SpendingDetailEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSpendingDetail(entity: SpendingDetailEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSpendingsCrossRef(join: List<SpendingDetailsCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSpendingCrossRef(crossRef: SpendingDetailsCrossRef)

    @Query(
        "SELECT * FROM ${SpendingDetailsCrossRef.TABLE_NAME} " +
                "WHERE ${SpendingEntity.COLUMN_ID} = :id",
    )

    suspend fun getSpendingsCrossRef(id: String): List<SpendingDetailsCrossRef>

    @Query(
        "SELECT * FROM ${SpendingDetailsCrossRef.TABLE_NAME} " +
                "WHERE ${SpendingDetailEntity.COLUMN_ID} = :id",
    )
    suspend fun getDetailCrossRefs(id: String): List<SpendingDetailsCrossRef>

    @Query(
        "SELECT * FROM ${SpendingEntity.TABLE_NAME} " +
                "WHERE ${SpendingEntity.COLUMN_ID} = :id",
    )
    suspend fun getSpending(id: String): SpendingEntity

    @Query(
        "SELECT * FROM ${SpendingDetailEntity.TABLE_NAME} " +
                "WHERE ${SpendingDetailEntity.COLUMN_ID} IN (:iDs)",
    )
    suspend fun getSpendingDetails(iDs: List<String>): List<SpendingDetailEntity>

    @Query(
        "UPDATE ${SpendingEntity.TABLE_NAME} " +
                "SET ${SpendingEntity.COLUMN_IS_PLANNED} = :isPlanned " +
                "WHERE ${SpendingEntity.COLUMN_ID} = :spendingId",
    )
    suspend fun updatePlanned(spendingId: String, isPlanned: Boolean)

    @Query(
        "DELETE FROM ${SpendingEntity.TABLE_NAME} " +
                "WHERE ${SpendingEntity.COLUMN_IS_DUPLICATE} = $TRUE",
    )
    suspend fun deleteDuplicates()

    @Query("SELECT * FROM ${SpendingDetailEntity.TABLE_NAME}")
    suspend fun getAllSpendingDetails(): List<SpendingDetailEntity>

    @Query(
        "DELETE FROM ${SpendingDetailsCrossRef.TABLE_NAME} " +
                "WHERE ${SpendingEntity.COLUMN_ID} = :spendingId " +
                "AND ${SpendingDetailEntity.COLUMN_ID} = :detailId",
    )
    suspend fun deleteCrossRef(spendingId: String, detailId: String)

    @Query(
        "DELETE FROM ${SpendingDetailEntity.TABLE_NAME} WHERE " +
                "${SpendingDetailEntity.COLUMN_ID} = :id",
    )
    suspend fun deleteSpendingDetail(id: String)

    @Query(
        "SELECT * FROM ${SpendingDetailEntity.TABLE_NAME} " +
                "WHERE ${SpendingDetailEntity.COLUMN_NAME} = :name " +
                "AND ${SpendingDetailEntity.COLUMN_AMOUNT} = :amount",
    )
    suspend fun getSpendingDetailDuplicate(name: String, amount: Long): SpendingDetailEntity?

    @Query(
        "DELETE FROM ${SpendingEntity.TABLE_NAME} WHERE " +
                "${SpendingEntity.COLUMN_ID} = :id",
    )
    suspend fun deleteSpending(id: String)

    @Query(
        "SELECT * FROM ${SpendingEntity.TABLE_NAME} " +
                "WHERE ${SpendingEntity.COLUMN_CATEGORY} = :id ",
    )
    suspend fun getSpendingsByCategory(id: String): List<SpendingEntity>

    @Query(
        "SELECT * FROM ${SpendingEntity.TABLE_NAME} " +
                "WHERE ${SpendingEntity.COLUMN_IS_PLANNED} = :isPlanned " +
                "AND ${SpendingEntity.COLUMN_DATE} >= :from " +
                "AND ${SpendingEntity.COLUMN_DATE} <= :to",
    )
    suspend fun getSpendingsByDate(isPlanned: Boolean, from: Long, to: Long): List<SpendingEntity>

    @Query(
        "SELECT " +
                "MIN(${SpendingEntity.COLUMN_DATE}) AS ${DateRange.COLUMN_MIN}," +
                " MAX(${SpendingEntity.COLUMN_DATE}) AS ${DateRange.COLUMN_MAX} " +
                "FROM ${SpendingEntity.TABLE_NAME} " +
                "WHERE ${SpendingEntity.COLUMN_IS_PLANNED} = :isPlanned",
    )
    suspend fun getSpendingsMinMaxDate(isPlanned: Boolean): DateRange
}

