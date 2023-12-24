package com.faigenbloom.famillyspandings.spandings.show

import com.faigenbloom.famillyspandings.comon.toLocalDate
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail

typealias CategoriesMock = com.faigenbloom.famillyspandings.categories.Mock

class Mock {
    companion object {
        val mockSpendingForUI = SpendingForUI(
            id = "asdfasd",
            name = "Home",
            amount = 1000L,
            date = "01.11.2023".toLocalDate(),
            category = CategoriesMock.categoriesList[1],
            photoUri = null,
            details = listOf(
                SpendingDetail("asdfasd", "Food", "400"),
                SpendingDetail("asdfasdasd", "Shampoo", "35"),
                SpendingDetail("asdfasddddd", "Drops", "50"),
            ),
        )
    }
}
