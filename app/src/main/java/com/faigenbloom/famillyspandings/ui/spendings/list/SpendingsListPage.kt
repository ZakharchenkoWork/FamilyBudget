package com.faigenbloom.famillyspandings.ui.spendings.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.common.DynamicPlatesHolder
import com.faigenbloom.famillyspandings.common.TopBar
import com.faigenbloom.famillyspandings.domain.spendings.FilterType
import com.faigenbloom.famillyspandings.domain.spendings.Pattern
import com.faigenbloom.famillyspandings.domain.spendings.PlateSizeType
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme
import com.faigenbloom.ninepatch.painterResourceNinePath

@Composable
fun SpendingsListPage(
    modifier: Modifier = Modifier,
    state: SpendingsState,
    onOpenSpending: (String) -> Unit,
) {
    Column(modifier = modifier) {
        TopBar(
            title = stringResource(
                id = if (state.isPlannedListShown) {
                    R.string.spendings_planned_title
                } else {
                    R.string.spendings_previous_title
                },
            ),
        )
        if (state.isLoading.not()) {
            if (state.spendings.isEmpty()) {
                EmptySpendings(modifier = modifier)
            } else {
                DynamicPlatesHolder(
                    datedPatterns = state.spendings,
                    filterType = state.filterType,
                    onSpendingClicked = onOpenSpending,
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun EmptySpendings(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.padding(top = 32.dp),
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "",
        )
        Text(
            modifier = Modifier.padding(32.dp),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.spendings_no_spendings_message),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Image(
            modifier = Modifier.fillMaxHeight(),
            painter = painterResourceNinePath(id = R.drawable.icon_arrow_down),
            contentDescription = "",
        )
    }
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun SpandingsEmptyPagePreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            SpendingsListPage(
                onOpenSpending = {},
                state = SpendingsState(
                    spendings = emptyList(),
                    isPlannedListShown = false,
                    isLoading = false,
                    onPlannedSwitched = {},
                    filterType = FilterType.Daily(),
                ),
            )
        }
    }
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun SpandingsPagePreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            SpendingsListPage(
                onOpenSpending = {},
                state = SpendingsState(
                    spendings =
                    listOf(
                        listOf(
                            Pattern<SpendingCategoryUiData>(
                                listOf(
                                    PlateSizeType.SIZE_THREE_BY_ONE,
                                ),
                            ).apply {
                                items = listOf(
                                    mockSpendingsWithCategoryList[0],
                                )
                            },
                            Pattern<SpendingCategoryUiData>(
                                listOf(
                                    PlateSizeType.SIZE_TWO_BY_ONE,
                                ),
                            ).apply {
                                items = listOf(
                                    mockSpendingsWithCategoryList[0],
                                )
                            },
                            Pattern<SpendingCategoryUiData>(
                                listOf(
                                    PlateSizeType.SIZE_ONE_BY_ONE,
                                    PlateSizeType.SIZE_ONE_BY_ONE,
                                    PlateSizeType.SIZE_ONE_BY_ONE,
                                ),
                            ).apply {
                                items = listOf(
                                    mockSpendingsWithCategoryList[0],
                                    mockSpendingsWithCategoryList[1],
                                    mockSpendingsWithCategoryList[2],
                                )
                            },
                            Pattern<SpendingCategoryUiData>(
                                listOf(
                                    PlateSizeType.SIZE_ONE_BY_ONE,
                                    PlateSizeType.SIZE_ONE_BY_ONE,
                                ),
                            ).apply {
                                items = listOf(
                                    mockSpendingsWithCategoryList[0],
                                    mockSpendingsWithCategoryList[1],
                                )
                            },
                            Pattern<SpendingCategoryUiData>(
                                listOf(
                                    PlateSizeType.SIZE_ONE_BY_ONE,
                                ),
                            ).apply {
                                items = listOf(
                                    mockSpendingsWithCategoryList[0],
                                )
                            },
                        ),
                    ),
                    isPlannedListShown = false,
                    isLoading = false,
                    onPlannedSwitched = {},
                    filterType = FilterType.Daily(),
                ),
            )
        }
    }
}
