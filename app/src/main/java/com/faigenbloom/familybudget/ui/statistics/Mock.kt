package com.faigenbloom.familybudget.ui.statistics

import com.faigenbloom.familybudget.ui.categories.mockCategoriesList


val mockCategoriesSummaryList = mockCategoriesList.map {
    CategorySummaryUi(
        id = it.id,
        nameId = it.nameId,
        iconId = it.iconId,
        amount = (Math.random() * 900 + 100).toLong(),
        name = null,
        iconUri = null,
    )
}.filterIndexed { index: Int, _: CategorySummaryUi ->
    return@filterIndexed index != 0
}
