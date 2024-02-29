package com.faigenbloom.familybudget.domain

import android.util.Log
import com.faigenbloom.familybudget.MainDispatcherRule
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.MockDataSource
import com.faigenbloom.familybudget.datasources.db.entities.SpendingDetailsCrossRef
import com.faigenbloom.familybudget.datasources.firebase.CategoryNetworkSource
import com.faigenbloom.familybudget.datasources.firebase.FamilyNetworkSource
import com.faigenbloom.familybudget.datasources.firebase.NetworkDataSource
import com.faigenbloom.familybudget.datasources.firebase.SpendingsNetworkSource
import com.faigenbloom.familybudget.domain.details.GetSpendingDetailsByIdUseCase
import com.faigenbloom.familybudget.domain.details.SaveDetailsUseCase
import com.faigenbloom.familybudget.domain.mappers.DetailsMapper
import com.faigenbloom.familybudget.repositories.DetailsRepository
import com.faigenbloom.familybudget.repositories.mappers.SpendingDetailsSourceMapper
import com.faigenbloom.familybudget.ui.spendings.DetailUiData
import com.faigenbloom.familybudget.ui.spendings.edit.mockDetailsList
import com.google.firebase.firestore.FirebaseFirestore
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

class SaveDetailsUseCaseTest {
    private val mockSpendingId = "1"
    private val altSpendingId = "2"
    private val altDetailName = "COPY"
    private val altDetailId = "MOCK_GEN_ID"

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val detailsMapper = DetailsMapper()
    private val firestore: FirebaseFirestore = mock {}
    val mockEditedDetail = detailsMapper.forDB(
        mockDetailsList[0].copy(
            name = altDetailName,
        ),
    )
    val mockEditedDetailDuplicate = detailsMapper.forDB(
        mockDetailsList[0].copy(
            id = altDetailId,
            name = altDetailName,
        ),
    )
    private val dataSource: MockDataSource = mock {
        wheneverBlocking { it.getSpendingDetails(mockSpendingId) }
            .thenReturn(listOf(detailsMapper.forDB(mockDetailsList[0])))
    }

    private val idGeneratorUseCase: GenerateIdUseCase = mock {
        wheneverBlocking { it.invoke("") }.thenReturn(altDetailId)
        wheneverBlocking { it.invoke(mockSpendingId) }.thenReturn(mockSpendingId)
        wheneverBlocking { it.invoke(altDetailId) }.thenReturn(altDetailId)
        wheneverBlocking { it.invoke(mockDetailsList[0].id) }.thenReturn(mockDetailsList[0].id)
        wheneverBlocking { it.invoke(mockDetailsList[1].id) }.thenReturn(mockDetailsList[1].id)
    }
    private val detailsRepository: DetailsRepository = DetailsRepository(
        dataBaseDataSource = dataSource,
        networkDataSource = NetworkDataSource(
            familyNetworkSource = FamilyNetworkSource(
                firestore = firestore,
                idSource = IdSource(),
            ),
            spendingsNetworkSource = SpendingsNetworkSource(
                firestore = firestore,
                idSource = IdSource(),
            ),
            categoryNetworkSource = CategoryNetworkSource(
                firestore = firestore,
                idSource = IdSource(),
            ),
            idSource = IdSource(),
        ),
        spendingDetailsSourceMapper = SpendingDetailsSourceMapper(),
    )
    private val getSpendingDetailsUseCase: GetSpendingDetailsByIdUseCase<DetailUiData> =
        GetSpendingDetailsByIdUseCase(
            detailsRepository = detailsRepository,
            mapper = detailsMapper,
        )

    private val saveDetailsUseCase: SaveDetailsUseCase<DetailUiData> =
        SaveDetailsUseCase(
            detailsRepository = detailsRepository,
            detailsMapper = detailsMapper,
        )

    @Before
    fun setup() {
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
     *   If detail is new.
     *      Create new detail
     *      Add new connection
     */
    @Test
    fun `adding new detail on empty list working`() = runTest {
        wheneverBlocking { dataSource.getSpendingDetails(mockSpendingId) }
            .thenReturn(emptyList())
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
            .thenReturn(emptyList())

        saveDetailsUseCase(
            mockSpendingId,
            details = listOf(mockDetailsList[0]),
        )

        verify(dataSource).addSpendingDetail(
            detailsMapper.forDB(mockDetailsList[0]),
        )
        verify(dataSource).addCrossRef(
            SpendingDetailsCrossRef(
                mockSpendingId,
                mockDetailsList[0].id,
            ),
        )
        verify(dataSource)
            .getSpendingDetails(mockSpendingId)

        verify(dataSource)
            .getDetailCrossRefs(mockDetailsList[0].id)

        verifyNoMoreInteractions(dataSource)
    }

    /**
     *   If detail is old.
     *      If spending is new
     *          Add new connection
     */
    @Test
    fun `adding old detail on new spending`() = runTest {
        wheneverBlocking { dataSource.getSpendingDetails(mockSpendingId) }
            .thenReturn(emptyList())
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
            .thenReturn(
                listOf(
                    SpendingDetailsCrossRef(
                        altSpendingId,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        saveDetailsUseCase(
            mockSpendingId,
            details = listOf(mockDetailsList[0]),
        )

        verify(dataSource)
            .getDetailCrossRefs(mockDetailsList[0].id)

        verify(dataSource).addCrossRef(
            SpendingDetailsCrossRef(
                mockSpendingId,
                mockDetailsList[0].id,
            ),
        )
        verify(dataSource)
            .getSpendingDetails(mockSpendingId)

        verifyNoMoreInteractions(dataSource)
    }

    /**
     * If detail is old and switched to another fully(clicked on another)
     *      If old is used once
     *          Delete old detail
     *          Delete old connection
     *          Add new Connection
     */
    @Test
    fun `detail changed to another, but old used once`() = runTest {
        wheneverBlocking {
            dataSource.getDetailCrossRefs(mockDetailsList[1].id)
        }.thenReturn(
            listOf(
                SpendingDetailsCrossRef(
                    altSpendingId,
                    mockDetailsList[1].id,
                ),
            ),
        )
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
            .thenReturn(
                listOf(
                    SpendingDetailsCrossRef(
                        mockSpendingId,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        wheneverBlocking {
            dataSource.deleteCrossRef(
                SpendingDetailsCrossRef(
                    mockSpendingId,
                    mockDetailsList[0].id,
                ),
            )
        }.then {
            wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
                .thenReturn(
                    emptyList(),
                )
        }
        saveDetailsUseCase(
            mockSpendingId,
            details = listOf(mockDetailsList[1]),
        )

        verify(dataSource).getDetailCrossRefs(
            mockDetailsList[0].id,
        )

        verify(dataSource).getSpendingDetails(
            mockSpendingId,
        )
        verify(
            dataSource,
        ).getDetailCrossRefs(
            mockDetailsList[1].id,
        )

        verify(dataSource).addCrossRef(
            SpendingDetailsCrossRef(
                mockSpendingId,
                mockDetailsList[1].id,
            ),
        )
        verify(dataSource).deleteCrossRef(
            SpendingDetailsCrossRef(
                mockSpendingId,
                mockDetailsList[0].id,
            ),
        )
        verify(dataSource).deleteSpendingDetail(
            mockDetailsList[0].id,
        )
        verifyNoMoreInteractions(dataSource)
    }

    /**
     * If detail switched to another fully (clicked on old)
     *      If old is used multiple times
     *          Delete old connection
     *          Add new Connection
     */

    @Test
    fun `detail switched to another existing, but old used multiple times`() = runTest {
        wheneverBlocking {
            dataSource.getDetailCrossRefs(mockDetailsList[1].id)
        }.thenReturn(
            listOf(
                SpendingDetailsCrossRef(
                    mockSpendingId,
                    mockDetailsList[1].id,
                ),
            ),
        )
        wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
            .thenReturn(
                listOf(
                    SpendingDetailsCrossRef(
                        mockSpendingId,
                        mockDetailsList[0].id,
                    ),
                    SpendingDetailsCrossRef(
                        altSpendingId,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        wheneverBlocking {
            dataSource.deleteCrossRef(
                SpendingDetailsCrossRef(
                    mockSpendingId,
                    mockDetailsList[0].id,
                ),
            )
        }.then {
            wheneverBlocking { dataSource.getDetailCrossRefs(mockDetailsList[0].id) }
                .thenReturn(
                    listOf(
                        SpendingDetailsCrossRef(
                            altSpendingId,
                            mockDetailsList[0].id,
                        ),
                    ),
                )
        }
        saveDetailsUseCase(
            mockSpendingId,
            details = listOf(mockDetailsList[1]),
        )

        verify(
            dataSource,
        ).getDetailCrossRefs(
            mockDetailsList[0].id,
        )
        verify(
            dataSource,
        ).getDetailCrossRefs(
            mockDetailsList[1].id,
        )

        verify(dataSource).getSpendingDetails(
            mockSpendingId,
        )

        verify(dataSource).addCrossRef(
            SpendingDetailsCrossRef(
                mockSpendingId,
                mockDetailsList[1].id,
            ),
        )
        verify(dataSource).deleteCrossRef(
            SpendingDetailsCrossRef(
                mockSpendingId,
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
                        mockSpendingId,
                        mockDetailsList[0].id,
                    ),
                ),
            )

        saveDetailsUseCase(
            mockSpendingId,
            details = listOf(detailsMapper.forUI(mockEditedDetail)),
        )

        verify(dataSource).getSpendingDetails(
            mockSpendingId,
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
                        mockSpendingId,
                        mockDetailsList[0].id,
                    ),
                    SpendingDetailsCrossRef(
                        altSpendingId,
                        mockDetailsList[0].id,
                    ),
                ),
            )

        saveDetailsUseCase(
            mockSpendingId,
            details = listOf(detailsMapper.forUI(mockEditedDetail)),
        )

        verify(dataSource).getSpendingDetails(
            mockSpendingId,
        )
        verify(dataSource).getDetailCrossRefs(
            mockDetailsList[0].id,
        )
        verify(dataSource).getSpendingDetailDuplicate(
            mockEditedDetail,
        )
        verify(dataSource).addSpendingDetail(
            mockEditedDetail.copy(id = altDetailId),
        )
        verify(dataSource).deleteCrossRef(
            SpendingDetailsCrossRef(
                mockSpendingId,
                mockDetailsList[0].id,
            ),
        )
        verify(dataSource).addCrossRef(
            SpendingDetailsCrossRef(
                mockSpendingId,
                altDetailId,
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
                        mockSpendingId,
                        mockDetailsList[0].id,
                    ),
                    SpendingDetailsCrossRef(
                        altSpendingId,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        saveDetailsUseCase(
            mockSpendingId,
            details = listOf(detailsMapper.forUI(mockEditedDetail)),
        )

        verify(dataSource).getSpendingDetails(
            mockSpendingId,
        )
        verify(dataSource).getDetailCrossRefs(
            mockDetailsList[0].id,
        )
        verify(dataSource).getSpendingDetailDuplicate(
            mockEditedDetail,
        )

        verify(dataSource).deleteCrossRef(
            SpendingDetailsCrossRef(
                mockSpendingId,
                mockDetailsList[0].id,
            ),
        )
        verify(dataSource).addCrossRef(
            SpendingDetailsCrossRef(
                mockSpendingId,
                altDetailId,
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
                        mockSpendingId,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        saveDetailsUseCase(
            mockSpendingId,
            details = listOf(),
        )

        verify(dataSource).getSpendingDetails(
            mockSpendingId,
        )
        verify(dataSource).getDetailCrossRefs(
            mockDetailsList[0].id,
        )

        verify(dataSource).deleteCrossRef(
            SpendingDetailsCrossRef(
                mockSpendingId,
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
                        mockSpendingId,
                        mockDetailsList[0].id,
                    ),
                    SpendingDetailsCrossRef(
                        altSpendingId,
                        mockDetailsList[0].id,
                    ),
                ),
            )
        saveDetailsUseCase(
            mockSpendingId,
            details = listOf(),
        )

        verify(dataSource).getSpendingDetails(
            mockSpendingId,
        )
        verify(dataSource).getDetailCrossRefs(
            mockDetailsList[0].id,
        )

        verify(dataSource).deleteCrossRef(
            SpendingDetailsCrossRef(
                mockSpendingId,
                mockDetailsList[0].id,
            ),
        )
        verifyNoMoreInteractions(dataSource)
    }
}
