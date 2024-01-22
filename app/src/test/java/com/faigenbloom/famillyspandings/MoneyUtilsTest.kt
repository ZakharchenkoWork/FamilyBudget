package com.faigenbloom.famillyspandings

import com.faigenbloom.famillyspandings.comon.toLongMoney
import com.faigenbloom.famillyspandings.comon.toNormalizedMoney
import com.faigenbloom.famillyspandings.comon.toReadableMoney
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MoneyUtilsTest {
    @Test
    fun `Long_toReadableMoney() is correct`() {
        assertEquals("0.04", 4L.toReadableMoney())
        assertEquals("0.40", 40L.toReadableMoney())
        assertEquals("0.43", 43L.toReadableMoney())
        assertEquals("4.00", 400L.toReadableMoney())
        assertEquals("4.30", 430L.toReadableMoney())
        assertEquals("4.33", 433L.toReadableMoney())
    }

    @Test
    fun `String_toLongMoney() is correct`() {
        assertEquals(4L, "0.04".toLongMoney())
        assertEquals(40L, "0.40".toLongMoney())
        assertEquals(43L, "0.43".toLongMoney())
        assertEquals(400L, "4.00".toLongMoney())
        assertEquals(430L, "4.30".toLongMoney())
        assertEquals(436L, "4.36".toLongMoney())
        assertEquals(433L, "4.332".toLongMoney())
    }

    @Test
    fun `String_toNormalizedMoney() is correct`() {
        assertEquals("0.00", "".toNormalizedMoney())
        assertEquals("0.00", ".".toNormalizedMoney())
        assertEquals("0.10", ".1".toNormalizedMoney())
        assertEquals("5.00", "5".toNormalizedMoney())
        assertEquals("11.00", "0011".toNormalizedMoney())
        assertEquals("99.09", "00099.09".toNormalizedMoney())
        assertEquals("6.00", "6.".toNormalizedMoney())
        assertEquals("3.80", "3.8".toNormalizedMoney())
        assertEquals("32.57", "32.57".toNormalizedMoney())
        assertEquals("4332.29", "4332.2951".toNormalizedMoney())
    }
}
