package com.faigenbloom.famillyspandings.domain

import android.util.Log
import com.faigenbloom.famillyspandings.comon.toReadableDate
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Test


class NormalizeDateUseCaseTest {
    private val normalizeDateUseCase: NormalizeDateUseCase = NormalizeDateUseCase()
    private val mockDate: String = "24.03.2024"
    private val currentDate: Long = System.currentTimeMillis()

    @Before
    fun setup() {
        mockkStatic(Log::class)

        every { Log.e(any(), any()) } answers {
            println(arg<String>(1))
            0
        }
    }

    @Test
    fun `Can Normalize empty date as current Long`() {
        normalizeDateUseCase("") shouldNotBe null
        normalizeDateUseCase("") shouldBe currentDate.toReadableDate()
    }

    @Test
    fun `Can Normalize real date as Long`() {
        normalizeDateUseCase(mockDate) shouldNotBe null
        normalizeDateUseCase(mockDate) shouldBe mockDate
    }

    @Test
    fun `Can Normalize random symbols as Long`() {
        normalizeDateUseCase("asdg") shouldNotBe null
        normalizeDateUseCase("asdg") shouldBe currentDate.toReadableDate()
    }
}
