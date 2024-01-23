package com.faigenbloom.famillyspandings.ui.statistics

import BarChart
import BarData
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.palette.graphics.Palette
import coil.compose.rememberImagePainter
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.common.StripeBar
import com.faigenbloom.famillyspandings.common.TopBar
import com.faigenbloom.famillyspandings.common.toReadableDate
import com.faigenbloom.famillyspandings.common.toReadableMoney
import com.faigenbloom.famillyspandings.common.ui.MoneyTextTransformation
import com.faigenbloom.famillyspandings.common.ui.PieChart
import com.faigenbloom.famillyspandings.common.ui.PieChartData
import com.faigenbloom.famillyspandings.domain.statistics.FilterType
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme
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
        if (state.isPieChartOpened) {
            PieChartContent(state)
        } else {
            BarChartContent(state)
        }
    }
}

@Composable
private fun BarChartContent(state: StatisicsState) {
    val barWidth = 80.dp
    val barLeftPadding = 16.dp
    val rememberScrollState: LazyListState = rememberLazyListState()
    Column {
        TopText(state)
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
                                        Divider(
                                            modifier = Modifier
                                                .width(barLeftPadding)
                                                .height(barWidth),
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            thickness = barLeftPadding,
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
fun TopText(state: StatisicsState) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .size(32.dp)
                .aspectRatio(1f)
                .clickable {
                    state.onDateMoved(false)
                },
            painter = painterResource(id = R.drawable.icon_arrow_back),
            contentDescription = null,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = if (state.filterType is FilterType.Daily ||
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
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondaryContainer,
        )
        Image(
            modifier = Modifier
                .size(32.dp)
                .aspectRatio(1f)
                .clickable {
                    state.onDateMoved(true)
                },
            painter = painterResource(id = R.drawable.icon_arrow_next),
            contentDescription = null,
        )
    }
}

@Composable
private fun PieChartContent(state: StatisicsState) {
    TopText(state = state)
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


@Composable
fun asColor(categorySummary: CategorySummaryUi): Color {
    return categorySummary.iconUri?.let {
        asColor(iconUri = it.toUri())
    } ?: categorySummary.iconId?.let {
        asColor(icon = it)
    } ?: asColor(
        categorySummary.name ?: categorySummary.nameId?.let { stringResource(id = it) } ?: "",
    )
}

@Composable
fun asColor(name: String): Color {
    return Color(name.hashCode() and 0xFFFFFF)
}

@Composable
fun asColor(icon: Int): Color {
    return Color(
        Palette.from(
            BitmapFactory.decodeResource(
                LocalContext.current.resources,
                icon,
            ),
        ).generate().dominantSwatch?.rgb ?: Color.White.toArgb(),
    )
}

@Composable
fun asColor(iconUri: Uri): Color {
    return Color(
        Palette.from(
            BitmapFactory.decodeStream(
                LocalContext.current.contentResolver.openInputStream(iconUri),
            ),
        ).generate().dominantSwatch?.rgb ?: Color.White.toArgb(),
    )
}

@Composable
fun CategorySummaryUi.getImage(): Painter {
    return this.iconUri?.let {
        rememberImagePainter(it)
    } ?: this.iconId?.let {
        painterResource(id = it)
    } ?: painterResource(id = R.drawable.icon_photo)
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
