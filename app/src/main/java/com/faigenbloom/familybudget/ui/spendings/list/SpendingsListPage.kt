package com.faigenbloom.familybudget.ui.spendings.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.TopBar
import com.faigenbloom.familybudget.common.isEmpty
import com.faigenbloom.familybudget.common.ui.Loading
import com.faigenbloom.familybudget.domain.spendings.DatedList
import com.faigenbloom.familybudget.domain.spendings.Pattern
import com.faigenbloom.familybudget.domain.spendings.PlateSizeType
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme
import com.faigenbloom.ninepatch.painterResourceNinePath
import kotlinx.coroutines.flow.flowOf

@Composable
fun SpendingsListPage(
    modifier: Modifier = Modifier,
    state: SpendingsState,
    onOpenSpending: (String) -> Unit,
) {
    Column(modifier = modifier) {
        TopBar(
            title = stringResource(
                id = if (state.filterType.isPlanned) {
                    R.string.spendings_planned_title
                } else {
                    R.string.spendings_previous_title
                },
            ),
        )
        val lazyPagingItems = state.spendingsPager.collectAsLazyPagingItems()

        if (state.isLoading.value.not()) {
            if (lazyPagingItems.isEmpty()) {
                if (lazyPagingItems.loadState.source.refresh is LoadState.NotLoading &&
                    lazyPagingItems.loadState.append.endOfPaginationReached
                    && lazyPagingItems.itemCount < 1
                ) {
                    EmptySpendings(modifier = modifier)
                } else {
                    Loading(true)
                }
            } else {
                DynamicPlatesHolder(
                    datedPatterns = lazyPagingItems,
                    filterType = state.filterType,
                    onSpendingClicked = onOpenSpending,
                )
            }
        } else {
            Loading(true)
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

@Preview(showBackground = true)
@Composable
fun SpandingsEmptyPagePreview() {
    FamillySpandingsTheme {
        Surface {
            SpendingsListPage(
                onOpenSpending = {},
                state = SpendingsState(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpandingsPagePreview() {
    FamillySpandingsTheme {
        Surface {
            SpendingsListPage(
                onOpenSpending = {},
                state = SpendingsState(
                    spendingsPager = flowOf(
                        value = PagingData.from(
                            listOf(
                                DatedList(
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
                            ),
                        ),
                    ),
                    isLoading = mutableStateOf(false),
                ),
            )
        }
    }
}
