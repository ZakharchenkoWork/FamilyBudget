package com.faigenbloom.famillyspandings.domain

import com.faigenbloom.famillyspandings.domain.spendings.dividers.DayGroupDivider
import com.faigenbloom.famillyspandings.domain.spendings.dividers.MonthGroupDivider
import com.faigenbloom.famillyspandings.domain.spendings.dividers.YearGroupDivider
import com.faigenbloom.famillyspandings.ui.spandings.list.mockSpendingsWithCategoryList
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DividersTests {
    val dayDivider = DayGroupDivider()
    val monthDivider = MonthGroupDivider()
    val yearDivider = YearGroupDivider()

    @Test
    fun `dayDivider can sort by days`() =
        runTest {
            val listByDate = dayDivider.prepareGroups(
                mockSpendingsWithCategoryList,
            )
            listByDate.size shouldBe 12
            listByDate[0].size shouldBe 6
        }

    @Test
    fun `monthDivider can sort by month`() =
        runTest {
            val listByDate = monthDivider.prepareGroups(
                mockSpendingsWithCategoryList,
            )
            listByDate.size shouldBe 2
            listByDate[0].size shouldBe 6
        }

    @Test
    fun `yearDivider can sort by year`() =
        runTest {
            val listByDate = yearDivider.prepareGroups(
                mockSpendingsWithCategoryList,
            )
            listByDate.size shouldBe 1
            listByDate[0].size shouldBe mockSpendingsWithCategoryList.size
        }
}
