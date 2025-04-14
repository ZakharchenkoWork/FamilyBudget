package com.faigenbloom.familybudget

import com.faigenbloom.familybudget.common.findDatesBetween
import com.faigenbloom.familybudget.common.getMonthEndDate
import com.faigenbloom.familybudget.common.getMonthStartDate
import com.faigenbloom.familybudget.common.toLongDate
import com.faigenbloom.familybudget.common.toReadableDate
import com.faigenbloom.familybudget.datasources.db.entities.RepeatOptions
import io.kotest.matchers.shouldBe
import org.junit.Test


class DateTests {

    @Test
    fun `getMonthEndDate is correct`() {

        "22.01.2024".toLongDate().getMonthEndDate().toReadableDate() shouldBe "31.01.2024"

        "22.02.2024".toLongDate().getMonthEndDate().toReadableDate() shouldBe "29.02.2024"

        "22.02.2024".toLongDate().getMonthEndDate(past = 1).toReadableDate() shouldBe "31.01.2024"

        "22.02.2024".toLongDate().getMonthEndDate(future = 1).toReadableDate() shouldBe "31.03.2024"

    }

    @Test
    fun `getMonthStartDate is correct`() {

        "22.01.2024".toLongDate().getMonthStartDate().toReadableDate() shouldBe "01.01.2024"

        "22.02.2024".toLongDate().getMonthStartDate().toReadableDate() shouldBe "01.02.2024"

        "22.01.2024".toLongDate().getMonthStartDate(past = 1).toReadableDate() shouldBe "01.12.2023"

        "22.02.2024".toLongDate().getMonthStartDate(future = 1)
            .toReadableDate() shouldBe "01.03.2024"
    }
    @Test
    fun `findDatesBetween days is correct`() {
        val start = "01.01.2024".toLongDate()
        val end = "28.02.2024".toLongDate()
        val current = "02.02.2024".toLongDate()

        val datesDaily = findDatesBetween(start, end, current, RepeatOptions.DAILY)
        datesDaily.size shouldBe 27
    }

    @Test
    fun `findDatesBetween weeks is correct`() {
        val start = "01.01.2024".toLongDate()
        val end = "28.02.2024".toLongDate()
        val current = "02.02.2024".toLongDate()

        val datesWeekly = findDatesBetween(start, end, current, RepeatOptions.WEEKLY)
        datesWeekly.size shouldBe 4
    }
    @Test
    fun `findDatesBetween Monthly is correct`() {
        val start = "01.01.2024".toLongDate()
        val end = "10.03.2024".toLongDate()
        val current = "02.01.2024".toLongDate()

        val datesMonthly = findDatesBetween(start, end, current, RepeatOptions.MONTHLY)
        datesMonthly.size shouldBe 3
    }
    @Test
    fun `findDatesBetween Yearly is correct`() {
        val start = "01.01.2024".toLongDate()
        val end = "10.03.2025".toLongDate()
        val current = "02.01.2024".toLongDate()

        val datesYearly = findDatesBetween(start, end, current, RepeatOptions.YEARLY)
        datesYearly.size shouldBe 2
    }
}
