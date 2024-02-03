package com.faigenbloom.familybudget.domain

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.Test


class GenerateIdUseCaseTest {
    private val idGeneratorUseCase: GenerateIdUseCase = GenerateIdUseCase()
    private val mockID: String = "asgd"

    @Test
    fun `Can generate new id`() {
        idGeneratorUseCase("") shouldNotBe null
    }

    @Test
    fun `Do not change old id`() {
        idGeneratorUseCase(mockID) shouldBe mockID
    }
}
