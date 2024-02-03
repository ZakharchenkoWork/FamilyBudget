package com.faigenbloom.familybudget.datasources.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.faigenbloom.familybudget.datasources.entities.SpendingDetailsCrossRef.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    primaryKeys = [SpendingEntity.COLUMN_ID, SpendingDetailEntity.COLUMN_ID],
)
data class SpendingDetailsCrossRef(
    @ColumnInfo(name = SpendingEntity.COLUMN_ID) val spendingId: String,
    @ColumnInfo(name = SpendingDetailEntity.COLUMN_ID) val detailId: String,
) {
    companion object {
        const val TABLE_NAME = "spending_details_cross_ref"
    }
}

data class SpendingWithDetails(
    @Embedded
    var spending: SpendingEntity,
    @Relation(
        parentColumn = SpendingEntity.COLUMN_ID,
        entity = SpendingDetailEntity::class,
        entityColumn = SpendingDetailEntity.COLUMN_ID,
        associateBy = Junction(
            value = SpendingDetailsCrossRef::class,
            parentColumn = SpendingEntity.COLUMN_ID,
            entityColumn = SpendingDetailEntity.COLUMN_ID,
        ),
    )
    var instructor: List<SpendingDetailEntity>,
)
