package com.faigenbloom.famillyspandings

import com.faigenbloom.famillyspandings.common.getMonthEndDate
import com.faigenbloom.famillyspandings.common.getMonthStartDate
import com.faigenbloom.famillyspandings.common.toLongDate
import com.faigenbloom.famillyspandings.common.toReadableDate
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
}
