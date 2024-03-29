package com.faigenbloom.familybudget.ui.statistics

import BarChart
import BarData
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.StripeBar
import com.faigenbloom.familybudget.common.TopBar
import com.faigenbloom.familybudget.common.toReadableDate
import com.faigenbloom.familybudget.common.toReadableMoney
import com.faigenbloom.familybudget.common.ui.AnimateTabs
import com.faigenbloom.familybudget.common.ui.DateSwitcherBar
import com.faigenbloom.familybudget.common.ui.Loading
import com.faigenbloom.familybudget.common.ui.MoneyTextTransformation
import com.faigenbloom.familybudget.common.ui.PieChart
import com.faigenbloom.familybudget.common.ui.PieChartData
import com.faigenbloom.familybudget.domain.statistics.FilterType
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme
import syncronizer
import java.util.Currency
import java.util.Locale

@Composable
fun StatisticsPage(state: StatisicsState) {
    Column {
        TopBar(
            title = stringResource(id = R.string.statistics),
        )
        StripeBar(
            textId = R.string.statistics_pie,
            secondTabTextId = R.string.statistics_bar,
            isLeftSelected = state.isPieChartOpened,
            onSelectionChanged = state.onPageChanged,
        )
        AnimateTabs(isLeftTab = state.isPieChartOpened) { isPieChartOpened ->
            if (isPieChartOpened) {
                PieChartContent(state)
            } else {
                BarChartContent(state)
            }
        }
    }
    val isLoading by remember { state.isLoading }
    Loading(isShown = isLoading)
}

@Composable
private fun BarChartContent(state: StatisicsState) {
    val barWidth = 80.dp
    val barLeftPadding = 16.dp
    val rememberScrollState: LazyListState = rememberLazyListState()
    Column {
        DateSwitcherBar(
            title = GetDateSwitcherTitle(state),
            onDateMoved = state.onDateMoved,
        )
        if (state.isNoDataToShow) {
            NoDataToShow()
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            ) {
                BarChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    linesColor = MaterialTheme.colorScheme.secondary,
                    barWidth = barWidth,
                    barLeftPadding = barLeftPadding,
                    rememberScrollState = rememberScrollState,
                    bars = state.categorySummary.map {
                        BarData(
                            value = it.barDataValue,
                            color = asColor(it),
                        )
                    },
                    sideLabel = {
                        Text(
                            modifier = Modifier.padding(end = 4.dp),
                            text = state.sideLabelValues[it],
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    topLabel = {
                        Text(
                            modifier = Modifier.padding(start = 57.dp),
                            text = state.currency.symbol,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                        )
                    },
                    bottomRow = { innerScrollState ->
                        LazyRow(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                )
                                .syncronizer(
                                    scope = rememberCoroutineScope(),
                                    fromState = rememberScrollState,
                                    toState = innerScrollState,
                                ),
                            state = rememberScrollState,
                            content = {
                                item {
                                    Spacer(
                                        modifier = Modifier.width(61.5.dp),
                                    )
                                    for (index in 0 until state.categorySummary.size) {
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = MaterialTheme.colorScheme.primaryContainer,
                                                )
                                                .padding(start = barLeftPadding)
                                                .padding(vertical = 16.dp),
                                        ) {
                                            Image(
                                                modifier = Modifier
                                                    .size(barWidth)
                                                    .aspectRatio(1f),
                                                painter = state.categorySummary[index].getImage(),
                                                contentScale = ContentScale.Crop,
                                                contentDescription = "",
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                            )
                                            .padding(vertical = 16.dp),
                                    ) {
                                        HorizontalDivider(
                                            modifier = Modifier
                                                .width(barLeftPadding)
                                                .height(barWidth),
                                            thickness = barLeftPadding,
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        )
                                    }
                                }
                            },
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun GetDateSwitcherTitle(state: StatisicsState) =
    if (state.filterType is FilterType.Daily ||
        (state.filterType is FilterType.Range &&
                state.filterType.from == state.filterType.to)
    ) {
        stringResource(
            id = R.string.statistics_small_title_day,
            state.filterType.from.toReadableDate(),
        )
    } else {
        stringResource(
            id = R.string.statistics_small_title_range,
            state.filterType.from.toReadableDate(),
            state.filterType.to.toReadableDate(),
        )
    }

@Composable
private fun PieChartContent(state: StatisicsState) {
    Column {
        DateSwitcherBar(
            title = GetDateSwitcherTitle(state),
            onDateMoved = state.onDateMoved,
        )
        if (state.isNoDataToShow) {
            NoDataToShow()
        } else {
            PieChart(
                chartData = state.categorySummary.map {
                    PieChartData(
                        valuePercent = it.amountPercent.toFloat(),
                        color = asColor(categorySummary = it),
                    )
                },
                central = {
                    Image(
                        modifier = Modifier.size(90.dp),
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = "",
                    )
                },
                label = { paddings, value ->
                    if (value.toInt() > 1) {
                        Text(
                            modifier = Modifier.padding(
                                paddings,
                            ),
                            text = "${value.toInt()}%",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                },
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = MoneyTextTransformation(state.currency.currencyCode).filter(state.sum),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.secondaryContainer,
            )
            BottomRow(state)
        }
    }
}

@Composable
fun BottomRow(state: StatisicsState) {
    LazyRow(modifier = Modifier.height(80.dp)) {
        item {
            Column(
                modifier = Modifier
                    .width(40.dp)
                    .height(80.dp)
                    .background(color = MaterialTheme.colorScheme.primary),
            ) { }
        }
        items(state.categorySummary.size) {
            val categorySummary = state.categorySummary[it]
            Row {
                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .aspectRatio(1f),
                    painter = categorySummary.getImage(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "",
                )
                Column(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                        ),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = categorySummary.amount.toReadableMoney(),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = state.currency.currencyCode,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    )
                }
            }
        }
    }
}

@Composable
fun NoDataToShow() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.spendings_no_spendings_message),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondaryContainer,
        )
    }
}

@Preview
@Composable
fun StatisticsBarChartPagePreview() {
    FamillySpandingsTheme {
        Surface {
            StatisticsPage(
                state = StatisicsState(
                    categorySummary = mockCategoriesSummaryList,
                    sum = "7500",
                    max = 1000,
                    currency = Currency.getInstance(Locale.getDefault()),
                    isPieChartOpened = false,
                    onPageChanged = {},
                    sideLabelValues = Array(10) { "$it+0" },
                    filterType = FilterType.Monthly(),
                    isNoDataToShow = false,
                    rangeClicked = {},
                    yearlyClicked = {},
                    monthlyClicked = {},
                    dailyClicked = {},
                    onDateMoved = {},
                ),
            )
        }
    }
}

@Preview
@Composable
fun StatisticsPieChartPagePreview() {
    FamillySpandingsTheme {
        Surface {
            StatisticsPage(
                state = StatisicsState(
                    categorySummary = mockCategoriesSummaryList,
                    sum = "7500.00",
                    max = 1000,
                    currency = Currency.getInstance(Locale.getDefault()),
                    filterType = FilterType.Monthly(),
                    isPieChartOpened = true,
                    isNoDataToShow = false,
                    onPageChanged = {},
                    sideLabelValues = Array(10) { "$it+0" },
                    rangeClicked = {},
                    yearlyClicked = {},
                    monthlyClicked = {},
                    dailyClicked = {},
                    onDateMoved = {},
                ),
            )
        }
    }
}

@Preview
@Composable
fun StatisticsNoDataPagePreview() {
    FamillySpandingsTheme {
        Surface {
            StatisticsPage(
                state = StatisicsState(
                    categorySummary = emptyList(),
                    sum = "7500.00",
                    max = 1000,
                    currency = Currency.getInstance(Locale.getDefault()),
                    filterType = FilterType.Monthly(),
                    isPieChartOpened = true,
                    isNoDataToShow = true,
                    onPageChanged = {},
                    sideLabelValues = Array(10) { "$it+0" },
                    rangeClicked = {},
                    yearlyClicked = {},
                    monthlyClicked = {},
                    dailyClicked = {},
                    onDateMoved = {},
                ),
            )
        }
    }
}
