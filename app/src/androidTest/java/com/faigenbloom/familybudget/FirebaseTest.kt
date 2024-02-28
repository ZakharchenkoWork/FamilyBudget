package com.faigenbloom.familybudget

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.firebase.FamilyNetworkSource
import com.faigenbloom.familybudget.datasources.firebase.models.FamilyModel
import com.faigenbloom.familybudget.datasources.firebase.models.PersonModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseTest : BaseTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    override fun getTestRule() = composeTestRule
    lateinit var firestore: FamilyNetworkSource

    @Before
    fun setup() {
        FirebaseApp.initializeApp(composeTestRule.activity)
        firestore = FamilyNetworkSource(Firebase.firestore, IdSource())
    }

    @Test
    fun can_create_user_with_family() {
        runBlocking {
            firestore.createFamily(FamilyModel("fam1", "Zakh"))
            firestore.createFamilyMember(
                PersonModel("pers1", "fam1", "kos", "Zakh", hidden = false),
                ArrayList(),
            )
            firestore.connectFamily("pers1", "fam1")

            val familyId = firestore.getFamilyId("pers1") ?: ""
            assert(familyId == "fam1")
            val family = firestore.getFamily(familyId)
            assert(family?.name == "Zakh")
            val person = firestore.getPerson("fam1", "pers1")

            assert(person?.name == "kos")
        }
    }

    @Test
    fun can_read_persons_collection() {
        runBlocking {
            val persons = firestore.getFamilyMembers("fam1")
            persons?.let {
                assert(it[0]?.name == "kos")
            } ?: kotlin.run { assert(false) }


        }
    }

}
