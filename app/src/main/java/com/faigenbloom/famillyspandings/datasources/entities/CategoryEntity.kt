package com.faigenbloom.famillyspandings.datasources.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.datasources.entities.CategoryEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_IS_DEFAULT) val isDefault: Boolean,
    @ColumnInfo(name = COLUMN_NAME) val name: String? = null,
    @ColumnInfo(name = COLUMN_PHOTO) val photoUri: String? = null,
) {
    companion object {
        const val TABLE_NAME = "categories"
        const val COLUMN_IS_DEFAULT = "is_default"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHOTO = "photo"
    }
}

enum class DefaultCategories(val nameId: Int, val iconId: Int) {
    BEAUTY(

        nameId = R.string.category_beauty,
        iconId = R.drawable.beauty,
    ),
    EDUCATION(
        nameId = R.string.category_education,
        iconId = R.drawable.education,
    ),
    ENTERTAIMENT(
        nameId = R.string.category_entertaiment,
        iconId = R.drawable.entertaiment,
    ),
    FOOD(
        nameId = R.string.category_food,
        iconId = R.drawable.food,
    ),
    HEALTH(
        nameId = R.string.category_health,
        iconId = R.drawable.health,
    ),
    HOME(
        nameId = R.string.category_home,
        iconId = R.drawable.home,
    ),
    PETS(
        nameId = R.string.category_pets,
        iconId = R.drawable.pets,
    ),
    SHOPPING(
        nameId = R.string.category_shopping,
        iconId = R.drawable.shopping,
    ),
    SPORT(
        nameId = R.string.category_sport,
        iconId = R.drawable.sport,
    ),
    TRANSPORT(
        nameId = R.string.category_transport,
        iconId = R.drawable.transport,
    ),
    TRAVEL(
        nameId = R.string.category_travel,
        iconId = R.drawable.travel,
    ),
}
