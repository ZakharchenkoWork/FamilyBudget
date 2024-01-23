package com.faigenbloom.famillyspandings

import com.faigenbloom.famillyspandings.common.toLongMoney
import com.faigenbloom.famillyspandings.common.toNormalizedMoney
import com.faigenbloom.famillyspandings.common.toReadableMoney
import io.kotest.matchers.shouldBe
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MoneyUtilsTest {
    @Test
    fun `Long_toReadableMoney() is correct`() {
        4L.toReadableMoney() shouldBe "0.04"
        40L.toReadableMoney() shouldBe "0.40"
        43L.toReadableMoney() shouldBe "0.43"
        400L.toReadableMoney() shouldBe "4.00"
        430L.toReadableMoney() shouldBe "4.30"
        433L.toReadableMoney() shouldBe "4.33"
    }

    @Test
    fun `String_toLongMoney() is correct`() {
        "0.04".toLongMoney() shouldBe 4L
        "0.40".toLongMoney() shouldBe 40L
        "0.43".toLongMoney() shouldBe 43L
        "4.00".toLongMoney() shouldBe 400L
        "4.30".toLongMoney() shouldBe 430L
        "4.36".toLongMoney() shouldBe 436L
        "4.332".toLongMoney() shouldBe 433L
    }

    @Test
    fun `String_toNormalizedMoney() is correct`() {
        "".toNormalizedMoney() shouldBe "0.00"
        ".".toNormalizedMoney() shouldBe "0.00"
        ".1".toNormalizedMoney() shouldBe "0.10"
        "5".toNormalizedMoney() shouldBe "5.00"
        "0011".toNormalizedMoney() shouldBe "11.00"
        "00099.09".toNormalizedMoney() shouldBe "99.09"
        "6.".toNormalizedMoney() shouldBe "6.00"
        "3.8".toNormalizedMoney() shouldBe "3.80"
        "32.57".toNormalizedMoney() shouldBe "32.57"
        "4332.2951".toNormalizedMoney() shouldBe "4332.29"
    }
}
