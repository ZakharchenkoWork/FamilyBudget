package com.faigenbloom.famillyspandings

import com.faigenbloom.famillyspandings.categories.CategoryData
import com.faigenbloom.famillyspandings.comon.toReadableDate
import com.faigenbloom.famillyspandings.comon.toReadableMoney
import com.faigenbloom.famillyspandings.datasources.MockDataSource
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailsCrossRef
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.id.IdGenerator
import com.faigenbloom.famillyspandings.spandings.edit.SpendingsEditRepository
import com.faigenbloom.famillyspandings.spandings.edit.mockDetailsList
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.wheneverBlocking

const val ALT_SPENDING_ID = "2"
const val ALT_DETAIL_NAME = "COPY"
const val ALT_DETAIL_ID = "MOCK_GEN_ID"

class SpendingRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockSpending = SpendingEntity(
        id = "1",
        name = "",
        amount = 1L,
        date = 1643673600000L,
        categoryId = "asdf",
        photoUri = null,
        isPlanned = false,
        isHidden = false,
    )
    val mockEditedDetail = mockDetailsList[0].copy(
        name = ALT_DETAIL_NAME,
    ).mapToEntity()
    val mockEditedDetailDuplicate = mockDetailsList[0].copy(
        id = ALT_DETAIL_ID,
        name = ALT_DETAIL_NAME,
    ).mapToEntity()

    private val dataSource: MockDataSource = mock {
        wheneverBlocking { it.getSpending(mockSpending.id) }.thenReturn(mockSpending)
        wheneverBlocking { it.getSpendingDetails(mockSpending.id) }
            .thenReturn(listOf(mockDetailsList[0].mapToEntity()))
    }
    private val idGenerator: IdGenerator = mock {
        wheneverBlocking { it.checkOrGenId("") }.thenReturn(ALT_DETAIL_ID)
    }
    val repository: SpendingsEditRepository = SpendingsEditRepository(
        dataSource = dataSource,
        idGenerator = idGenerator,
    )

    @Test
    fun `getSpendingById working`() = runTest {
        repository.getSpending(mockSpending.id) shouldNotBe null
    }

    /**
     *   If detail is new.
     *      Create new detail
     *      Add new connection
     */
    @Test
    fun `adding new detail on empty list working`() = runTest {
        wheneverBlocking { dataSource.getSpendingDetails(mockSpending.id) }
            .thenReturn(emptyList())

        repository.saveSpending(
            id = mockSpending.id,
            name = mockSpending.name,
            amount = mockSpending.amount.toReadableMoney(),
            date = mockSpending.date.toReadableDate(),
            category = CategoryData(mockSpending.categoryId),
            photoUri = null,
            isHidden = mockSpending.isHidden,
            details = listOf(mockDetailsList[1].mapToEntity()),
        )
        verify(dataSource).saveSpending(
            mockSpending,
        )
        verify(dataSource).addSpendingDetail(
            mockDetailsList[1].mapToEntity(),
        )
        verify(dataSource).addCrossRef(
            SpendingDetailsCrossRef(
                mockSpending.id,
                mockDetailsList[1].id,
            ),
        )
        verify(dataSource)
            .getSpendingDetails(mockSpending.id)

        verifyNoMoreInteractions(dataSource)
    }

    /**
     *   If detail is old.
     *      If spending is new
     *          Add new connection
     */
    @Test
    fun `adding old detail on new spending`() = runTest {
        wheneverBlocking { dataSource.getSpendingDetails(mockSpending.id) }
            .thenReturn(emptyList())
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
            .thenReturn(
                listOf(
                    SpendingDetailsCrossRef(
                        ALT_SPENDING_ID,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        repository.saveSpending(
            id = mockSpending.id,
            name = mockSpending.name,
            amount = mockSpending.amount.toReadableMoney(),
            date = mockSpending.date.toReadableDate(),
            category = CategoryData(mockSpending.categoryId),
            photoUri = null,
            isHidden = mockSpending.isHidden,
            details = listOf(mockDetailsList[0].mapToEntity()),
        )
        verify(dataSource).saveSpending(
            mockSpending,
        )

        verify(dataSource)
            .getDetailCrossRefs(mockDetailsList[0].id)

        verify(dataSource).addCrossRef(
            SpendingDetailsCrossRef(
                mockSpending.id,
                mockDetailsList[0].id,
            ),
        )
        verify(dataSource)
            .getSpendingDetails(mockSpending.id)

        verifyNoMoreInteractions(dataSource)
    }

    /**
     * If detail switched to another fully
     *      If old is used once
     *          Delete old detail
     *          Delete old connection
     *          Add new Connection
     *          Add new Detail
     */
    @Test
    fun `detail changed to another, but old used once`() = runTest {
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
            .thenReturn(
                listOf(
                    SpendingDetailsCrossRef(
                        mockSpending.id,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        wheneverBlocking {
            dataSource.deleteCrossRef(
                SpendingDetailsCrossRef(
                    mockSpending.id,
                    mockDetailsList[0].id,
                ),
            )
        }.then {
            wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
                .thenReturn(
                    emptyList(),
                )
        }
        repository.saveSpending(
            id = mockSpending.id,
            name = mockSpending.name,
            amount = mockSpending.amount.toReadableMoney(),
            date = mockSpending.date.toReadableDate(),
            category = CategoryData(mockSpending.categoryId),
            photoUri = null,
            isHidden = mockSpending.isHidden,
            details = listOf(mockDetailsList[1].mapToEntity()),
        )
        verify(dataSource).saveSpending(
            mockSpending,
        )
        verify(dataSource).getDetailCrossRefs(
            mockDetailsList[0].id,
        )

        verify(dataSource).getSpendingDetails(
            mockSpending.id,
        )
        verify(dataSource).addSpendingDetail(
            mockDetailsList[1].mapToEntity(),
        )

        verify(dataSource).addCrossRef(
            SpendingDetailsCrossRef(
                mockSpending.id,
                mockDetailsList[1].id,
            ),
        )
        verify(dataSource).deleteCrossRef(
            SpendingDetailsCrossRef(
                mockSpending.id,
                mockDetailsList[0].id,
            ),
        )
        verify(dataSource).deleteSpendingDetail(
            mockDetailsList[0].id,
        )
        verifyNoMoreInteractions(dataSource)
    }

    /**
     * If detail switched to another fully
     *      If old is used multiple times
     *          Delete old connection
     *          Add new Connection
     *          Add new Detail
     */

    @Test
    fun `detail changed to another, but old used multiple times`() = runTest {
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
            .thenReturn(
                listOf(
                    SpendingDetailsCrossRef(
                        mockSpending.id,
                        mockDetailsList[0].id,
                    ),
                    SpendingDetailsCrossRef(
                        ALT_SPENDING_ID,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        wheneverBlocking {
            dataSource.deleteCrossRef(
                SpendingDetailsCrossRef(
                    mockSpending.id,
                    mockDetailsList[0].id,
                ),
            )
        }.then {
            wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
                .thenReturn(
                    listOf(
                        SpendingDetailsCrossRef(
                            ALT_SPENDING_ID,
                            mockDetailsList[0].id,
                        ),
                    ),
                )
        }
        repository.saveSpending(
            id = mockSpending.id,
            name = mockSpending.name,
            amount = mockSpending.amount.toReadableMoney(),
            date = mockSpending.date.toReadableDate(),
            category = CategoryData(mockSpending.categoryId),
            photoUri = null,
            isHidden = mockSpending.isHidden,
            details = listOf(mockDetailsList[1].mapToEntity()),
        )
        verify(dataSource).saveSpending(
            mockSpending,
        )
        verify(
            dataSource,
        ).getDetailCrossRefs(
            mockDetailsList[0].id,
        )

        verify(dataSource).getSpendingDetails(
            mockSpending.id,
        )
        verify(dataSource).addSpendingDetail(
            mockDetailsList[1].mapToEntity(),
        )

        verify(dataSource).addCrossRef(
            SpendingDetailsCrossRef(
                mockSpending.id,
                mockDetailsList[1].id,
            ),
        )
        verify(dataSource).deleteCrossRef(
            SpendingDetailsCrossRef(
                mockSpending.id,
                mockDetailsList[0].id,
            ),
        )
        verifyNoMoreInteractions(dataSource)
    }

    /**
     * If detail is old but edited (name changed)
     *     If old is used once
     *         Replacing old detail with same id.
     */
    @Test
    fun `detail can be edited, when used once`() = runTest {
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
            .thenReturn(
                listOf(
                    SpendingDetailsCrossRef(
                        mockSpending.id,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        repository.saveSpending(
            id = mockSpending.id,
            name = mockSpending.name,
            amount = mockSpending.amount.toReadableMoney(),
            date = mockSpending.date.toReadableDate(),
            category = CategoryData(mockSpending.categoryId),
            photoUri = null,
            isHidden = mockSpending.isHidden,
            details = listOf(mockEditedDetail),
        )

        verify(dataSource).saveSpending(
            mockSpending,
        )
        verify(dataSource).getSpendingDetails(
            mockSpending.id,
        )
        verify(dataSource).getDetailCrossRefs(
            mockDetailsList[0].id,
        )
        verify(dataSource).addSpendingDetail(
            mockEditedDetail,
        )
        verifyNoMoreInteractions(dataSource)
    }

    /**
     * If detail is old but edited (name changed)
     *      If old detail is used multiple times
     *          Check duplicates by name and amount of new detail
     *              If there is no duplicates
     *                  Add new detail
     *                  Add new connection
     *                  Delete old connection
     */
    @Test
    fun `detail can be edited, when used multiple times, without duplicates of new`() = runTest {
        wheneverBlocking { dataSource.getSpendingDetailDuplicate(mockEditedDetail) }
            .thenReturn(null)
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
            .thenReturn(
                listOf(
                    SpendingDetailsCrossRef(
                        mockSpending.id,
                        mockDetailsList[0].id,
                    ),
                    SpendingDetailsCrossRef(
                        ALT_SPENDING_ID,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        repository.saveSpending(
            id = mockSpending.id,
            name = mockSpending.name,
            amount = mockSpending.amount.toReadableMoney(),
            date = mockSpending.date.toReadableDate(),
            category = CategoryData(mockSpending.categoryId),
            photoUri = null,
            isHidden = mockSpending.isHidden,
            details = listOf(
                mockEditedDetail,
            ),
        )

        verify(dataSource).saveSpending(
            mockSpending,
        )
        verify(dataSource).getSpendingDetails(
            mockSpending.id,
        )
        verify(dataSource).getDetailCrossRefs(
            mockDetailsList[0].id,
        )
        verify(dataSource).getSpendingDetailDuplicate(
            mockEditedDetail,
        )
        verify(dataSource).addSpendingDetail(
            mockEditedDetail.copy(id = ALT_DETAIL_ID),
        )
        verify(dataSource).deleteCrossRef(
            SpendingDetailsCrossRef(
                mockSpending.id,
                mockDetailsList[0].id,
            ),
        )
        verify(dataSource).addCrossRef(
            SpendingDetailsCrossRef(
                mockSpending.id,
                ALT_DETAIL_ID,
            ),
        )
        verifyNoMoreInteractions(dataSource)
    }

    /**
     * If detail is old but edited (name changed)
     *      If old detail is used multiple times
     *          Check duplicates by name and amount of new detail
     *              If there is a duplicate
     *                  Add new connection
     *                  Delete old connection
     */
    @Test
    fun `detail can be edited, when used multiple times, with duplicates of new`() = runTest {
        wheneverBlocking { dataSource.getSpendingDetailDuplicate(mockEditedDetail) }
            .thenReturn(mockEditedDetailDuplicate)
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
            .thenReturn(
                listOf(
                    SpendingDetailsCrossRef(
                        mockSpending.id,
                        mockDetailsList[0].id,
                    ),
                    SpendingDetailsCrossRef(
                        ALT_SPENDING_ID,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        repository.saveSpending(
            id = mockSpending.id,
            name = mockSpending.name,
            amount = mockSpending.amount.toReadableMoney(),
            date = mockSpending.date.toReadableDate(),
            category = CategoryData(mockSpending.categoryId),
            photoUri = null,
            isHidden = mockSpending.isHidden,
            details = listOf(
                mockEditedDetail,
            ),
        )

        verify(dataSource).saveSpending(
            mockSpending,
        )
        verify(dataSource).getSpendingDetails(
            mockSpending.id,
        )
        verify(dataSource).getDetailCrossRefs(
            mockDetailsList[0].id,
        )
        verify(dataSource).getSpendingDetailDuplicate(
            mockEditedDetail,
        )

        verify(dataSource).deleteCrossRef(
            SpendingDetailsCrossRef(
                mockSpending.id,
                mockDetailsList[0].id,
            ),
        )
        verify(dataSource).addCrossRef(
            SpendingDetailsCrossRef(
                mockSpending.id,
                ALT_DETAIL_ID,
            ),
        )
        verifyNoMoreInteractions(dataSource)
    }

    /**
     *  If detail is removed
     *      If old detail used once
     *          Delete old detail
     *          Delete old connection
     */
    @Test
    fun `detail can be deleted, when used once`() = runTest {
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
            .thenReturn(
                listOf(
                    SpendingDetailsCrossRef(
                        mockSpending.id,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        repository.saveSpending(
            id = mockSpending.id,
            name = mockSpending.name,
            amount = mockSpending.amount.toReadableMoney(),
            date = mockSpending.date.toReadableDate(),
            category = CategoryData(mockSpending.categoryId),
            photoUri = null,
            isHidden = mockSpending.isHidden,
            details = listOf(),
        )

        verify(dataSource).saveSpending(
            mockSpending,
        )
        verify(dataSource).getSpendingDetails(
            mockSpending.id,
        )
        verify(dataSource).getDetailCrossRefs(
            mockDetailsList[0].id,
        )

        verify(dataSource).deleteCrossRef(
            SpendingDetailsCrossRef(
                mockSpending.id,
                mockDetailsList[0].id,
            ),
        )
        verify(dataSource).deleteSpendingDetail(
            mockDetailsList[0].id,
        )
        verifyNoMoreInteractions(dataSource)
    }

    /**
     *  If detail is removed
     *      If old detail used multiple times
     *          Delete old connection
     */
    @Test
    fun `detail can be deleted, when used multiple times`() = runTest {
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
            .thenReturn(
                listOf(
                    SpendingDetailsCrossRef(
                        mockSpending.id,
                        mockDetailsList[0].id,
                    ),
                    SpendingDetailsCrossRef(
                        ALT_SPENDING_ID,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        repository.saveSpending(
            id = mockSpending.id,
            name = mockSpending.name,
            amount = mockSpending.amount.toReadableMoney(),
            date = mockSpending.date.toReadableDate(),
            category = CategoryData(mockSpending.categoryId),
            photoUri = null,
            isHidden = mockSpending.isHidden,
            details = listOf(),
        )

        verify(dataSource).saveSpending(
            mockSpending,
        )
        verify(dataSource).getSpendingDetails(
            mockSpending.id,
        )
        verify(dataSource).getDetailCrossRefs(
            mockDetailsList[0].id,
        )

        verify(dataSource).deleteCrossRef(
            SpendingDetailsCrossRef(
                mockSpending.id,
                mockDetailsList[0].id,
            ),
        )
        verifyNoMoreInteractions(dataSource)
    }
}
