package com.faigenbloom.familybudget.domain

import com.faigenbloom.familybudget.MainDispatcherRule
import com.faigenbloom.familybudget.datasources.MockDataSource
import com.faigenbloom.familybudget.datasources.db.entities.SpendingEntity
import com.faigenbloom.familybudget.datasources.firebase.NetworkDataSource
import com.faigenbloom.familybudget.domain.categories.DeleteCategoryUseCase
import com.faigenbloom.familybudget.repositories.CategoriesRepository
import com.faigenbloom.familybudget.repositories.mappers.CategorySourceMapper
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
        ownerId = "",
        isDuplicate = false,
    )
    private val dataSource: MockDataSource = mock {}
    private val networkDataSource: NetworkDataSource = mock {}
    private val categorySourceMapper: CategorySourceMapper = mock {}

    private val deleteCategoryUseCase: DeleteCategoryUseCase =
        DeleteCategoryUseCase(
            categoriesRepository = CategoriesRepository(
                dataSource,
                networkDataSource,
                categorySourceMapper,
            ),
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
