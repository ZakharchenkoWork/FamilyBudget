package com.faigenbloom.familybudget

import androidx.compose.ui.text.intl.Locale
import com.faigenbloom.familybudget.common.findDatesBetween
import com.faigenbloom.familybudget.common.getMonthEndDate
import com.faigenbloom.familybudget.common.getMonthStartDate
import com.faigenbloom.familybudget.common.toLongDate
import com.faigenbloom.familybudget.common.toReadableDate
import com.faigenbloom.familybudget.datasources.db.entities.RepeatOptions
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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
        val end = "23.02.2024".toLongDate()
        val spendingDate = "20.02.2024".toLongDate()
        // 20.02.2024 is excluded, 21.02.2024, 22.02.2024 and 23.02.2024 are included
        val datesDaily = findDatesBetween(start, end, spendingDate, RepeatOptions.DAILY)
        datesDaily.size shouldBe 3
    }
    @Test
    fun `findDatesBetween days is correct in past repeatable`() {
        val start = "01.01.2025".toLongDate()
        val end = "04.01.2025".toLongDate()
        val spendingDate = "10.04.2024".toLongDate()
        // 20.02.2024 is excluded, 21.02.2024, 22.02.2024 and 23.02.2024 are included
        val datesDaily = findDatesBetween(start, end, spendingDate, RepeatOptions.DAILY)
        datesDaily.size shouldBe 4
        datesDaily[0].toReadableDate() shouldBe "01.01.2025"
        datesDaily[1].toReadableDate() shouldBe "02.01.2025"
        datesDaily[2].toReadableDate() shouldBe "03.01.2025"
        datesDaily[3].toReadableDate() shouldBe "04.01.2025"
    }

    @Test
    fun `findDatesBetween weeks is correct`() {
        val start = "01.01.2024".toLongDate()
        val end = "28.02.2024".toLongDate()
        val current = "02.02.2024".toLongDate()

        val datesWeekly = findDatesBetween(start, end, current, RepeatOptions.WEEKLY)
        datesWeekly.size shouldBe 3
    }

    @Test
    fun `findDatesBetween weeks is correct in past repeatable`() {
        val start = "01.04.2025".toLongDate()
        val end = "01.05.2025".toLongDate()
        val current = "03.02.2025".toLongDate()

        val datesWeekly = findDatesBetween(start, end, current, RepeatOptions.WEEKLY)

        datesWeekly.size shouldBe 4

        datesWeekly[0].toReadableDate() shouldBe "07.04.2025"
        datesWeekly[1].toReadableDate() shouldBe "14.04.2025"
        datesWeekly[2].toReadableDate() shouldBe "21.04.2025"
        datesWeekly[3].toReadableDate() shouldBe "28.04.2025"
    }

    @Test
    fun `findDatesBetween Monthly is correct `() {
        val start = "01.01.2024".toLongDate()
        val end = "10.03.2024".toLongDate()
        val current = "02.01.2024".toLongDate()

        val datesMonthly = findDatesBetween(start, end, current, RepeatOptions.MONTHLY)
        datesMonthly[0].toReadableDate() shouldBe "02.02.2024"
        datesMonthly[1].toReadableDate() shouldBe "02.03.2024"
        datesMonthly.size shouldBe 2
    }

    @Test
    fun `findDatesBetween Monthly is correct in past repeatable`() {
        val start = "01.04.2024".toLongDate()
        val end = "01.05.2024".toLongDate()
        val current = "10.01.2024".toLongDate()

        val datesMonthly = findDatesBetween(start, end, current, RepeatOptions.MONTHLY)
        datesMonthly[0].toReadableDate() shouldBe "10.04.2024"

        datesMonthly.size shouldBe 1
    }

    @Test
    fun `findDatesBetween Yearly is correct`() {
        val start = "01.01.2024".toLongDate()
        val end = "10.03.2025".toLongDate()
        val current = "02.01.2024".toLongDate()

        val datesYearly = findDatesBetween(start, end, current, RepeatOptions.YEARLY)
        datesYearly.size shouldBe 1
    }

    @Test
    fun `findDatesBetween Yearly is correct in past repeatable`() {
        val start = "01.01.2024".toLongDate()
        val end = "01.01.2025".toLongDate()
        val current = "12.11.2022".toLongDate()

        val datesYearly = findDatesBetween(start, end, current, RepeatOptions.YEARLY)
        datesYearly[0].toReadableDate() shouldBe "12.11.2024"
        datesYearly.size shouldBe 1
    }
}
