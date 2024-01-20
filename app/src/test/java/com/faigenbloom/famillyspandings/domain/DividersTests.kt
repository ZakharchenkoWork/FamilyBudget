package com.faigenbloom.famillyspandings.domain

import com.faigenbloom.famillyspandings.comon.getCurrentDate
import com.faigenbloom.famillyspandings.comon.toLongDate
import com.faigenbloom.famillyspandings.domain.spendings.dividers.DayGroupDivider
import com.faigenbloom.famillyspandings.domain.spendings.dividers.MonthGroupDivider
import com.faigenbloom.famillyspandings.domain.spendings.dividers.YearGroupDivider
import com.faigenbloom.famillyspandings.ui.spandings.list.mockSpendingsWithCategoryList
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DividersTests {
    val dayDivider = DayGroupDivider(0L, getCurrentDate())
    val monthDivider = MonthGroupDivider(0L, getCurrentDate())
    val yearDivider = YearGroupDivider(0L, getCurrentDate())

    @Test
    fun `dayDivider can sort by days`() =
        runTest {
            val listByDate = dayDivider.prepareGroups(
                list = mockSpendingsWithCategoryList,
            )
            listByDate.size shouldBe 12
            listByDate[0].size shouldBe 6
        }

    @Test
    fun `dayDivider can cut range`() =
        runTest {
            val listByDate = DayGroupDivider(
                startDate = "14.10.2023".toLongDate(),
                endDate = "14.10.2023".toLongDate(),
            ).prepareGroups(
                mockSpendingsWithCategoryList,
            )
            listByDate.size shouldBe 1
            listByDate[0].size shouldBe 1
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
    fun `monthDivider can cut range`() =
        runTest {
            val listByDate = MonthGroupDivider(
                startDate = "01.10.2023".toLongDate(),
                endDate = "14.11.2023".toLongDate(),
            ).prepareGroups(
                mockSpendingsWithCategoryList,
            )
            listByDate.size shouldBe 1
            listByDate[0].size shouldBe 24
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

    @Test
    fun `yearDivider can cut range`() =
        runTest {
            val listByDate = YearGroupDivider(
                startDate = "01.10.2023".toLongDate(),
                endDate = "14.11.2023".toLongDate(),
            ).prepareGroups(
                mockSpendingsWithCategoryList,
            )
            listByDate.size shouldBe 1
            listByDate[0].size shouldBe 24
        }
}
