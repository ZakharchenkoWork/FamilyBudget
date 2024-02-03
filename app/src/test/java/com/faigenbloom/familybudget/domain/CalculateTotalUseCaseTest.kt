package com.faigenbloom.familybudget.domain

import com.faigenbloom.familybudget.ui.spendings.edit.mockDetailsList
import io.kotest.matchers.shouldBe
import org.junit.Test


class CalculateTotalUseCaseTest {
    private val calculateTotalUseCase: CalculateTotalUseCase = CalculateTotalUseCase()
    private val mockAmount: String = "15.00"
    private val mockCalculatedAmount: String = "485.00"
    private val mockedDetailsList = listOf(
        mockDetailsList[0],
        mockDetailsList[1],
        mockDetailsList[2],
    )

    @Test
    fun `When amount is manual returns manual amount`() {
        calculateTotalUseCase(true, emptyList(), mockAmount) shouldBe mockAmount
        calculateTotalUseCase(
            true,
            mockedDetailsList,
            mockAmount,
        ) shouldBe mockAmount
    }

    @Test
    fun `When amount is not manual calculates`() {
        calculateTotalUseCase(false, emptyList(), mockAmount) shouldBe "0.00"
        calculateTotalUseCase(
            false,
            mockedDetailsList,
            mockAmount,
        ) shouldBe mockCalculatedAmount
    }
}
