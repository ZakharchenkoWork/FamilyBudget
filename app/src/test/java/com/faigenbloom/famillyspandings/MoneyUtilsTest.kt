package com.faigenbloom.famillyspandings

import com.faigenbloom.famillyspandings.comon.toLongMoney
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
        assertEquals(4L.toReadableMoney(), "0.04")
        assertEquals(40L.toReadableMoney(), "0.40")
        assertEquals(43L.toReadableMoney(), "0.43")
        assertEquals(400L.toReadableMoney(), "4.00")
        assertEquals(430L.toReadableMoney(), "4.30")
        assertEquals(433L.toReadableMoney(), "4.33")
    }

    @Test
    fun `String_toLongMoney() is correct`() {
        assertEquals("0.04".toLongMoney(), 4L)
        assertEquals("0.40".toLongMoney(), 40L)
        assertEquals("0.43".toLongMoney(), 43L)
        assertEquals("4.00".toLongMoney(), 400L)
        assertEquals("4.30".toLongMoney(), 430L)
        assertEquals("4.33".toLongMoney(), 433L)
    }
}
