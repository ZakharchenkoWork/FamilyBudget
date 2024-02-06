package com.faigenbloom.familybudget.domain

import android.util.Log
import com.faigenbloom.familybudget.MainDispatcherRule
import com.faigenbloom.familybudget.datasources.MockDataSource
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailsCrossRef
import com.faigenbloom.familybudget.domain.spendings.DeleteSpendingUseCase
import com.faigenbloom.familybudget.repositories.DetailsRepository
import com.faigenbloom.familybudget.repositories.SpendingsRepository
import com.faigenbloom.familybudget.ui.spendings.SpendingUiData
import com.faigenbloom.familybudget.ui.spendings.edit.mockDetailsList
import com.faigenbloom.familybudget.ui.spendings.mappers.DetailsMapper
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.wheneverBlocking

class DeleteSpendingUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val spendingWithNoDetailsId = "spendingWithNoDetailsId"
    private val spendingAlternativeId = "spendingAlternativeId"
    private val mockSpending = SpendingUiData(
        id = "1",
        name = "",
        amount = "1",
        date = "21.02.2026",
        categoryId = "asdf",
        photoUri = null,
        isPlanned = false,
        isManualTotal = false,
        isHidden = false,
    )

    private val mockDetail = DetailsMapper().forDB(mockDetailsList[0])
    private val dataSource: MockDataSource = mock {}

    private val deleteSpendingUseCase: DeleteSpendingUseCase =
        DeleteSpendingUseCase(
            spendingsRepository = SpendingsRepository(dataSource),
            detailsRepository = DetailsRepository(dataSource),
        )

    @Before
    fun setUp() {
        mockkStatic(Log::class)

        every { Log.e(any(), any()) } answers {
            println(arg<String>(1))
            0
        }
        every { Log.d(any(), any()) } answers {
            println(arg<String>(1))
            0
        }
    }

    /**
     *  If spending has NO details just delete this spending
     */
    @Test
    fun `can delete spending without details`() = runTest {
        wheneverBlocking { dataSource.getSpendingDetails(spendingWithNoDetailsId) }
            .thenReturn(emptyList())

        deleteSpendingUseCase(
            spendingWithNoDetailsId,
        )

        verify(dataSource)
            .getSpendingDetails(spendingWithNoDetailsId)

        verify(dataSource)
            .deleteSpending(spendingWithNoDetailsId)

        verifyNoMoreInteractions(dataSource)
    }

    /**
     * If spending has details which are used only in THIS spending
     *      must delete detail and connection
     */
    @Test
    fun `can delete spending deleting details too, `() = runTest {
        wheneverBlocking { dataSource.getSpendingDetails(mockSpending.id) }
            .thenReturn(listOf(mockDetail))
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetail.id) }
            .thenReturn(
                listOf(
                    SpendingDetailsCrossRef(mockSpending.id, mockDetail.id),
                ),
            )

        deleteSpendingUseCase(
            mockSpending.id,
        )

        verify(dataSource)
            .getSpendingDetails(mockSpending.id)
        verify(dataSource)
            .getDetailCrossRefs(mockDetail.id)

        verify(dataSource)
            .deleteSpending(mockSpending.id)

        verify(dataSource)
            .deleteSpendingDetail(mockDetail.id)

        verify(dataSource)
            .deleteCrossRef(SpendingDetailsCrossRef(mockSpending.id, mockDetail.id))

        verifyNoMoreInteractions(dataSource)
    }

    /**
     * If spending has details which are used in OTHER spending
     *          must delete only connection
     */
    @Test
    fun `can delete spending deleting only connections, `() = runTest {
        wheneverBlocking { dataSource.getSpendingDetails(mockSpending.id) }
            .thenReturn(listOf(mockDetail))
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetail.id) }
            .thenReturn(
                listOf(
                    SpendingDetailsCrossRef(mockSpending.id, mockDetail.id),
                    SpendingDetailsCrossRef(spendingAlternativeId, mockDetail.id),
                ),
            )

        deleteSpendingUseCase(
            mockSpending.id,
        )

        verify(dataSource)
            .getSpendingDetails(mockSpending.id)
        verify(dataSource)
            .getDetailCrossRefs(mockDetail.id)

        verify(dataSource)
            .deleteSpending(mockSpending.id)

        verify(dataSource)
            .deleteCrossRef(SpendingDetailsCrossRef(mockSpending.id, mockDetail.id))

        verifyNoMoreInteractions(dataSource)
    }
}
