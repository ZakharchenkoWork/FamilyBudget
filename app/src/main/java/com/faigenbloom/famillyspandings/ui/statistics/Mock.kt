package com.faigenbloom.famillyspandings.ui.statistics

import com.faigenbloom.famillyspandings.ui.categories.mockCategoriesList


val mockCategoriesSummaryList = mockCategoriesList.map {
    CategorySummary(
        id = it.id,
        nameId = it.nameId,
        iconId = it.iconId,
        amount = (Math.random() * 900 + 100).toLong(),
        name = null,
        iconUri = null,
    )
}.filterIndexed { index: Int, _: CategorySummary ->
    return@filterIndexed index != 0
}
