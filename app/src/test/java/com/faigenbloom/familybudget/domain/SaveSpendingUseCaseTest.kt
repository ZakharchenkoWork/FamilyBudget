package com.faigenbloom.familybudget.domain

import android.util.Log
import com.faigenbloom.familybudget.MainDispatcherRule
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.MockDataSource
import com.faigenbloom.familybudget.datasources.firebase.CategoryNetworkSource
import com.faigenbloom.familybudget.datasources.firebase.FamilyNetworkSource
import com.faigenbloom.familybudget.datasources.firebase.NetworkDataSource
import com.faigenbloom.familybudget.datasources.firebase.SpendingsNetworkSource
import com.faigenbloom.familybudget.domain.spendings.SaveSpendingUseCase
import com.faigenbloom.familybudget.repositories.SpendingsRepository
import com.faigenbloom.familybudget.repositories.mappers.SpendingDetailsSourceMapper
import com.faigenbloom.familybudget.repositories.mappers.SpendingSourceMapper
import com.faigenbloom.familybudget.ui.spendings.SpendingUiData
import com.faigenbloom.familybudget.ui.spendings.mappers.SpendingsMapper
import com.google.firebase.firestore.FirebaseFirestore
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.wheneverBlocking

class SaveSpendingUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val firestore: FirebaseFirestore = mock {}
    private val spendingsMapper: SpendingsMapper = SpendingsMapper()
    private val altSpendingId = "2"

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
        ownerId = "",
        isDuplicate = false,
    )
    private val idGeneratorUseCase: GenerateIdUseCase = mock {
        wheneverBlocking { it.invoke("") }.thenReturn(altSpendingId)
        wheneverBlocking { it.invoke(mockSpending.id) }.thenReturn(mockSpending.id)
    }
    private val dataSource: MockDataSource = mock {
        wheneverBlocking { it.getSpending(mockSpending.id) }.thenReturn(
            spendingsMapper.forDB(
                mockSpending,
            ),
        )
    }
    private val spendingsRepository: SpendingsRepository = SpendingsRepository(
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
        spendingSourceMapper = SpendingSourceMapper(),
        detailsSourceMapper = SpendingDetailsSourceMapper(),
        idSource = IdSource(),
    )
    private val saveSpendingUseCase: SaveSpendingUseCase<SpendingUiData> =
        SaveSpendingUseCase(
            idGeneratorUseCase = idGeneratorUseCase,
            spendingsRepository = spendingsRepository,
            spendingsMapper = spendingsMapper,
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

    @Test
    fun `saving old spending is working and returns old id`() = runTest {
        val spendingId = saveSpendingUseCase(mockSpending)
        verify(dataSource).saveSpending(
            spendingsMapper.forDB(mockSpending),
        )
        spendingId shouldBe mockSpending.id

    }

    @Test
    fun `saving new spending is working and returns new id`() = runTest {
        val spendingId = saveSpendingUseCase(mockSpending.copy(id = ""))
        verify(dataSource).saveSpending(
            spendingsMapper.forDB(mockSpending).copy(id = altSpendingId),
        )
        spendingId shouldBe altSpendingId
    }
}
