package com.faigenbloom.famillyspandings.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.comon.StripeBar
import com.faigenbloom.famillyspandings.comon.TopBar
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun BudgetPage(state: BudgetState) {
    Column {
        TopBar(title = stringResource(id = R.string.budget_title))
        StripeBar(
            textId = R.string.budget_personal,
            secondTabTextId = R.string.budget_family,

        )
    }
}

@Preview
@Composable
fun StatisticsPagePreview() {
    FamillySpandingsTheme {
        Surface {
            BudgetPage(
                state = BudgetState(
                    familyTotal = "151",
                ),
            )
        }
    }
}
