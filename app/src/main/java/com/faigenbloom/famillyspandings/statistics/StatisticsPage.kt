package com.faigenbloom.famillyspandings.statistics

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.comon.PieChart
import com.faigenbloom.famillyspandings.comon.PieChartData
import com.faigenbloom.famillyspandings.comon.StripeBar
import com.faigenbloom.famillyspandings.comon.TopBar
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun StatisticsPage(state: StatisicsState) {
    Column {
        TopBar()
        StripeBar(textId = R.string.statistics)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.this_month_spending_s_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondaryContainer,
            )
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = "",
            )
        }
        PieChart(
            chartData = state.categorySummary.map {
                PieChartData(
                    valuePercent = it.amountPercent.toFloat(),
                    color = asColor(icon = it.iconId),
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
                Text(
                    modifier = Modifier
                        .padding(
                            paddings,
                        ),
                    text = "${value.toInt()} %",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
            },
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "${state.sum}",
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
            if (it != 0) {
                val categorySummary = state.categorySummary[it]
                Row {
                    Image(
                        modifier = Modifier.width(80.dp),
                        painter = painterResource(id = categorySummary.iconId ?: R.drawable.photo),
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
                            text = "${categorySummary.amount}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = "${categorySummary.amount}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun asColor(icon: Int?): Color {
    return icon?.let {
        Color(
            Palette.from(
                BitmapFactory.decodeResource(
                    LocalContext.current.resources,
                    icon,
                ),
            ).generate().dominantSwatch?.rgb ?: Color.White.toArgb(),
        )
    } ?: Color.White
}

@Preview
@Composable
fun StatisticsPagePreview() {
    FamillySpandingsTheme {
        Surface {
            StatisticsPage(
                state = StatisicsState(
                    categorySummary = Mock.categoriesList,
                    sum = 7500,
                ),
            )
        }
    }
}
