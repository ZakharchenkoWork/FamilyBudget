package com.faigenbloom.famillyspandings.spandings

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
import com.faigenbloom.famillyspandings.comon.DynamicPlatesHolder
import com.faigenbloom.famillyspandings.comon.Pattern
import com.faigenbloom.famillyspandings.comon.PlateSizeType
import com.faigenbloom.famillyspandings.comon.TopBar
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme
import com.faigenbloom.ninepatch.painterResourceNinePath

@Composable
fun SpendingsPage(
    modifier: Modifier = Modifier,
    state: SpendingsState,
    onOpenSpending: (String) -> Unit,
) {
    Column(modifier = modifier) {
        TopBar(
            title = stringResource(id = R.string.last_spendings_title),
            endIcon = if (state.isPlannedListShown) {
                R.drawable.icon_list_outlined
            } else {
                R.drawable.icon_list_planned_outlined
            },
            onEndIconCLicked = state.onPlannedSwitched,
        )
        if (state.isLoading.not()) {
            if (state.spendings.isEmpty()) {
                EmptySpendings(modifier = modifier)
            } else {
                DynamicPlatesHolder(
                    datedPatterns = state.spendings,
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
            /*painter = rememberImagePainter(
                ContextCompat.getDrawable(
                    LocalContext.current,
                    R.drawable.icon_arrow_down,
                ),
            ),*/
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
            SpendingsPage(
                onOpenSpending = {},
                state = SpendingsState(
                    spendings = emptyList(),
                    isPlannedListShown = false,
                    isLoading = false,
                    onPlannedSwitched = {},
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
            SpendingsPage(
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
                    isPlannedListShown = false,
                    isLoading = false,
                    onPlannedSwitched = {},
                ),
            )
        }
    }
}
