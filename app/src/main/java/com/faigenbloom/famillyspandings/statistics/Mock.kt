package com.faigenbloom.famillyspandings.statistics

typealias MockCategories = com.faigenbloom.famillyspandings.categories.Mock

class Mock {
    companion object {
        val categoriesList = MockCategories.categoriesList.map {
            CategorySummary(
                id = it.id,
                nameId = it.nameId,
                iconId = it.iconId,
                amount = (Math.random() * 900 + 100).toLong(),
            )
        }
    }
}
