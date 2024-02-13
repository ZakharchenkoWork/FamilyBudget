package com.faigenbloom.familybudget.datasources.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.datasources.db.entities.CategoryEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class CategoryEntity(
    @PrimaryKey()
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_IS_DEFAULT) val isDefault: Boolean,
    @ColumnInfo(name = COLUMN_HIDDEN) val isHidden: Boolean = false,
    @ColumnInfo(name = COLUMN_NAME) val name: String? = null,
    @ColumnInfo(name = COLUMN_PHOTO) val photoUri: String? = null,
) {
    companion object {
        const val TABLE_NAME = "categories"
        const val COLUMN_IS_DEFAULT = "is_default"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHOTO = "photo"
        const val COLUMN_HIDDEN = "is_hidden"
    }
}

enum class DefaultCategories(val nameId: Int, val iconId: Int, val iconBigId: Int) {
    BEAUTY(
        nameId = R.string.category_beauty,
        iconId = R.drawable.icon_beauty_small,
        iconBigId = R.drawable.icon_beauty_big,
    ),
    EDUCATION(
        nameId = R.string.category_education,
        iconId = R.drawable.icon_education_small,
        iconBigId = R.drawable.icon_education_big,
    ),
    ENTERTAIMENT(
        nameId = R.string.category_entertaiment,
        iconId = R.drawable.icon_entertaiment_small,
        iconBigId = R.drawable.icon_entertaiment_big,
    ),
    FOOD(
        nameId = R.string.category_food,
        iconId = R.drawable.icon_food_small,
        iconBigId = R.drawable.icon_food_big,
    ),
    HEALTH(
        nameId = R.string.category_health,
        iconId = R.drawable.icon_health_small,
        iconBigId = R.drawable.icon_health_big,
    ),
    HOME(
        nameId = R.string.category_home,
        iconId = R.drawable.icon_home_small,
        iconBigId = R.drawable.icon_home_big,
    ),
    PETS(
        nameId = R.string.category_pets,
        iconId = R.drawable.icon_pets_small,
        iconBigId = R.drawable.icon_pets_big,
    ),
    SHOPPING(
        nameId = R.string.category_shopping,
        iconId = R.drawable.icon_shopping_small,
        iconBigId = R.drawable.icon_shopping_big,
    ),
    SPORT(
        nameId = R.string.category_sport,
        iconId = R.drawable.icon_sport_small,
        iconBigId = R.drawable.icon_sport_big,
    ),
    TRANSPORT(
        nameId = R.string.category_transport,
        iconId = R.drawable.icon_transport_small,
        iconBigId = R.drawable.icon_transport_big,
    ),
    TRAVEL(
        nameId = R.string.category_travel,
        iconId = R.drawable.icon_travel_small,
        iconBigId = R.drawable.icon_travel_big,
    );

    companion object {
        fun getOrNull(key: String): DefaultCategories? {
            return DefaultCategories.values().firstOrNull {
                it.name == key
            }
        }
    }
}
