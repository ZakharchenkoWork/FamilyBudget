package com.faigenbloom.famillyspandings.domain

import com.faigenbloom.famillyspandings.MainDispatcherRule
import com.faigenbloom.famillyspandings.datasources.MockDataSource
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.domain.categories.DeleteCategoryUseCase
import com.faigenbloom.famillyspandings.repositories.CategoriesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.wheneverBlocking

class DeleteCategoryUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val categoryId = "categoryId"
    private val mockSpending = SpendingEntity(
        id = "1",
        name = "",
        amount = 1L,
        date = 10L,
        categoryId = categoryId,
        photoUri = null,
        isPlanned = false,
        isManualTotal = false,
        isHidden = false,
    )
    private val dataSource: MockDataSource = mock {}

    private val deleteCategoryUseCase: DeleteCategoryUseCase =
        DeleteCategoryUseCase(
            categoriesRepository = CategoriesRepository(dataSource),
        )

    @Test
    fun `can delete category when it is not used`() = runTest {
        wheneverBlocking { dataSource.getSpendingsByCategory(categoryId) }
            .thenReturn(emptyList())

        deleteCategoryUseCase(
            categoryId,
        )

        verify(dataSource)
            .getSpendingsByCategory(categoryId)

        verify(dataSource)
            .deleteCategory(categoryId)

        verifyNoMoreInteractions(dataSource)
    }

    @Test
    fun `can hide category if it has connections`() = runTest {
        wheneverBlocking { dataSource.getSpendingsByCategory(categoryId) }
            .thenReturn(listOf(mockSpending))

        deleteCategoryUseCase(
            categoryId,
        )

        verify(dataSource)
            .getSpendingsByCategory(categoryId)
        verify(dataSource)
            .changeCategoryHidden(categoryId, true)

        verifyNoMoreInteractions(dataSource)
    }
}
