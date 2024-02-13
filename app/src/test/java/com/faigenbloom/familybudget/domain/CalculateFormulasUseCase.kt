package com.faigenbloom.familybudget.domain

import com.faigenbloom.familybudget.MainDispatcherRule
import com.faigenbloom.familybudget.common.NO_ANSWER
import com.faigenbloom.familybudget.common.toJson
import com.faigenbloom.familybudget.datasources.db.entities.BudgetLineEntity
import com.faigenbloom.familybudget.domain.budget.CalculateFormulasUseCase
import com.faigenbloom.familybudget.domain.budget.FormulaEntity
import com.faigenbloom.familybudget.ui.budget.BudgetLabels
import com.faigenbloom.familybudget.ui.budget.Operation
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class CalculateFormulasUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val calculateFormulasUseCase = CalculateFormulasUseCase()


    @Test
    fun `returns empty list for empty list`() = runTest {
        calculateFormulasUseCase(emptyList()) shouldBe emptyList()
    }

    @Test
    fun `returns same list if there is no formulas`() = runTest {
        calculateFormulasUseCase(entities) shouldBe entities
    }

    @Test
    fun `calculate addition formula`() = runTest {
        val entitiesResultList = calculateFormulasUseCase(additionEntities)
        entitiesResultList[0].amount shouldBe 30L
        entitiesResultList[1].amount shouldBe 10L
        entitiesResultList[2].amount shouldBe 20L
    }

    @Test
    fun `calculate balance formula`() = runTest {
        val entitiesResultList = calculateFormulasUseCase(balanceEntities)
        entitiesResultList[0].amount shouldBe 15L
        entitiesResultList[1].amount shouldBe 50L
        entitiesResultList[2].amount shouldBe 20L
        entitiesResultList[3].amount shouldBe 15L
    }

    @Test
    fun `calculate formula inside formula`() = runTest {
        val entitiesResultList = calculateFormulasUseCase(formulaInsideFormulaEntities)
        entitiesResultList[0].amount shouldBe 30L
        entitiesResultList[1].amount shouldBe 15L
        entitiesResultList[2].amount shouldBe 50L
        entitiesResultList[3].amount shouldBe 20L
        entitiesResultList[4].amount shouldBe 15L
    }

    @Test
    fun `calculate formula with circular dependency should be detected`() = runTest {
        val entitiesResultList = calculateFormulasUseCase(formulaCircularEntities)
        entitiesResultList[0].repeatableId shouldBe "DoubleBalance"
        entitiesResultList[0].amount shouldBe NO_ANSWER
    }

    val entities = listOf(
        BudgetLineEntity(
            id = "asdfasd",
            repeatableId = "asdfasd",
            name = "asdfasdf",
            amount = 1000L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = null,
        ),
    )

    val additionEntities = listOf(
        BudgetLineEntity(
            id = "asdfasd",
            repeatableId = "asdfasd",
            name = "asdfasdf",
            amount = 1000L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = FormulaEntity(
                actions = listOf(
                    Operation.Addition,
                ),
                budgetLineIds = listOf(
                    "first",
                    "second",
                ),
            ).toJson(),
        ),
        BudgetLineEntity(
            id = "firasdst",
            repeatableId = "first",
            name = "asaaadfasdf",
            amount = 10L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = null,
        ),
        BudgetLineEntity(
            id = "sasdecond",
            repeatableId = "second",
            name = "asdfasddddf",
            amount = 20L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = null,
        ),
    )

    val balanceEntities = listOf(
        BudgetLineEntity(
            id = "asdasdfasdfasd",
            repeatableId = BudgetLabels.BALANCE.name,
            name = "asdfasdf",
            amount = 1000L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = FormulaEntity(
                actions = listOf(
                    Operation.Subtraction,
                    Operation.Subtraction,
                ),
                budgetLineIds = listOf(
                    BudgetLabels.PLANNED_BUDGET.name,
                    BudgetLabels.SPENT.name,
                    BudgetLabels.PLANNED_SPENDINGS.name,
                ),
            ).toJson(),
        ),
        BudgetLineEntity(
            id = "firasdasdst",
            repeatableId = BudgetLabels.PLANNED_BUDGET.name,
            name = "asaaadfasdf",
            amount = 50L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = null,
        ),
        BudgetLineEntity(
            id = "secasdfaond",
            repeatableId = BudgetLabels.SPENT.name,
            name = "asdfasddddf",
            amount = 20L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = null,
        ),
        BudgetLineEntity(
            id = "secasdfaond",
            repeatableId = BudgetLabels.PLANNED_SPENDINGS.name,
            name = "asdfasddddf",
            amount = 15L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = null,
        ),
    )
    val formulaInsideFormulaEntities = listOf(
        BudgetLineEntity(
            id = "asdasdfasdfasd",
            repeatableId = "DoubleBalance",
            name = "asdfasdf",
            amount = 1000L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = FormulaEntity(
                actions = listOf(
                    Operation.Addition,
                ),
                budgetLineIds = listOf(
                    BudgetLabels.BALANCE.name,
                    BudgetLabels.BALANCE.name,
                ),
            ).toJson(),
        ),
        BudgetLineEntity(
            id = "asdasdfasdfasd",
            repeatableId = BudgetLabels.BALANCE.name,
            name = "asdfasdf",
            amount = 1000L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = FormulaEntity(
                actions = listOf(
                    Operation.Subtraction,
                    Operation.Subtraction,
                ),
                budgetLineIds = listOf(
                    BudgetLabels.PLANNED_BUDGET.name,
                    BudgetLabels.SPENT.name,
                    BudgetLabels.PLANNED_SPENDINGS.name,
                ),
            ).toJson(),
        ),
        BudgetLineEntity(
            id = "firasdasdst",
            repeatableId = BudgetLabels.PLANNED_BUDGET.name,
            name = "asaaadfasdf",
            amount = 50L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = null,
        ),
        BudgetLineEntity(
            id = "secasdfaond",
            repeatableId = BudgetLabels.SPENT.name,
            name = "asdfasddddf",
            amount = 20L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = null,
        ),
        BudgetLineEntity(
            id = "secasdfaond",
            repeatableId = BudgetLabels.PLANNED_SPENDINGS.name,
            name = "asdfasddddf",
            amount = 15L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = null,
        ),
    )
    val formulaCircularEntities = listOf(
        BudgetLineEntity(
            id = "asdasdfasdfasd",
            repeatableId = "DoubleBalance",
            name = "asdfasdf",
            amount = 1000L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = FormulaEntity(
                actions = listOf(
                    Operation.Addition,
                ),
                budgetLineIds = listOf(
                    BudgetLabels.BALANCE.name,
                    BudgetLabels.BALANCE.name,
                ),
            ).toJson(),
        ),
        BudgetLineEntity(
            id = "asdasdfasdfasd",
            repeatableId = BudgetLabels.BALANCE.name,
            name = "asdfasdf",
            amount = 1000L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = FormulaEntity(
                actions = listOf(
                    Operation.Subtraction,
                    Operation.Subtraction,
                ),
                budgetLineIds = listOf(
                    BudgetLabels.PLANNED_BUDGET.name,
                    BudgetLabels.SPENT.name,
                    BudgetLabels.PLANNED_SPENDINGS.name,
                ),
            ).toJson(),
        ),
        BudgetLineEntity(
            id = "firasdasdst",
            repeatableId = BudgetLabels.PLANNED_BUDGET.name,
            name = "asaaadfasdf",
            amount = 50L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = FormulaEntity(
                actions = listOf(
                    Operation.Subtraction,
                    Operation.Subtraction,
                ),
                budgetLineIds = listOf(
                    BudgetLabels.PLANNED_BUDGET.name,
                    BudgetLabels.SPENT.name,
                    "DoubleBalance",
                ),
            ).toJson(),
        ),
        BudgetLineEntity(
            id = "secasdfaond",
            repeatableId = BudgetLabels.SPENT.name,
            name = "asdfasddddf",
            amount = 20L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = null,
        ),
        BudgetLineEntity(
            id = "secasdfaond",
            repeatableId = BudgetLabels.PLANNED_SPENDINGS.name,
            name = "asdfasddddf",
            amount = 15L,
            sortableDate = 0L,
            isForMonth = false,
            isForFamily = false,
            isDefault = false,
            formula = null,
        ),
    )
}
