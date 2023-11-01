@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.spandings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.comon.DynamicPlatesHolder
import com.faigenbloom.famillyspandings.comon.Pattern
import com.faigenbloom.famillyspandings.comon.PlateSizeType
import com.faigenbloom.famillyspandings.comon.TopBar
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun SpandingsPage(
    modifier: Modifier = Modifier,
    state: SpendingsState,
    onOpenSpending: (String) -> Unit,
) {
    Column(modifier = modifier) {
        TopBar(title = stringResource(id = R.string.last_spendings_title))
        DynamicPlatesHolder(
            datedPatterns = state.spendings,
            onSpendingClicked = onOpenSpending,
        )
    }
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun SpandingsPagePreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            SpandingsPage(
                onOpenSpending = {},
                state = SpendingsState(
                    spendings =
                    listOf(
                        listOf(
                            Pattern<SpendingData>(
                                listOf(
                                    PlateSizeType.SIZE_THREE_BY_ONE,
                                ),
                            ).apply {
                                items = listOf(
                                    Mock.spendingsList[0],
                                )
                            },
                            Pattern<SpendingData>(
                                listOf(
                                    PlateSizeType.SIZE_TWO_BY_ONE,
                                ),
                            ).apply {
                                items = listOf(
                                    Mock.spendingsList[0],
                                )
                            },
                            Pattern<SpendingData>(
                                listOf(
                                    PlateSizeType.SIZE_ONE_BY_ONE,
                                    PlateSizeType.SIZE_ONE_BY_ONE,
                                    PlateSizeType.SIZE_ONE_BY_ONE,
                                ),
                            ).apply {
                                items = listOf(
                                    Mock.spendingsList[0],
                                    Mock.spendingsList[1],
                                    Mock.spendingsList[2],
                                )
                            },
                            Pattern<SpendingData>(
                                listOf(
                                    PlateSizeType.SIZE_ONE_BY_ONE,
                                    PlateSizeType.SIZE_ONE_BY_ONE,
                                ),
                            ).apply {
                                items = listOf(
                                    Mock.spendingsList[0],
                                    Mock.spendingsList[1],
                                )
                            },
                            Pattern<SpendingData>(
                                listOf(
                                    PlateSizeType.SIZE_ONE_BY_ONE,
                                ),
                            ).apply {
                                items = listOf(
                                    Mock.spendingsList[0],
                                )
                            },
                        ),
                    ),
                ),
            )
        }
    }
}
