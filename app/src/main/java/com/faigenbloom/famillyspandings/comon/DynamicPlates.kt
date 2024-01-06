@file:OptIn(ExperimentalFoundationApi::class)

package com.faigenbloom.famillyspandings.comon

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.spandings.Mock
import com.faigenbloom.famillyspandings.spandings.SpendingData
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

const val ONE: Float = 1f
const val ONE_THIRD: Float = 0.33333334f
const val TWO_THIRDS: Float = 0.6666666f
const val HALF: Float = 0.5f
const val ONE_AND_ONE_THIRDS: Float = 1.3333334f
const val ONE_AND_TWO_THIRDS: Float = 1.6666666f
const val TWO: Float = 2f
const val THREE: Float = 3f

@Composable
fun DynamicPlatesHolder(
    datedPatterns: List<List<Pattern<SpendingData>>>,
    onSpendingClicked: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
    ) {
        repeat(datedPatterns.size) { dateIndex ->
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .background(MaterialTheme.colorScheme.secondary),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = datedPatterns[dateIndex][0].items[0].date.toReadable(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                ) {
                    for (pattern in datedPatterns[dateIndex]) {
                        GetPlates(
                            pattern = pattern,
                            onSpendingClicked = onSpendingClicked,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GetPlates(
    pattern: Pattern<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    val plates = pattern.plates
    when {
        plates.size == 1 &&
            plates[0] == PlateSizeType.SIZE_THREE_BY_THREE
        -> {
            Plate_3x3(
                spendingData = pattern.items[0],
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 2 &&
            plates[0] == PlateSizeType.SIZE_THREE_BY_TWO &&
            plates[1] == PlateSizeType.SIZE_THREE_BY_ONE

        -> {
            Plate_3x2_3x1(
                pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 &&
            plates[0] == PlateSizeType.SIZE_THREE_BY_TWO &&
            plates[1] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[2] == PlateSizeType.SIZE_ONE_BY_ONE

        -> {
            Plate_3x2_2x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 1 &&
            plates[0] == PlateSizeType.SIZE_THREE_BY_TWO
        -> {
            Plate_3x2(
                spendingData = pattern.items[0],
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 &&
            plates[0] == PlateSizeType.SIZE_TWO_BY_TWO &&
            plates[1] == PlateSizeType.SIZE_THREE_BY_ONE &&
            plates[2] == PlateSizeType.SIZE_TWO_BY_ONE
        -> {
            Plate_2x2_3x1_2x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 4 &&
            plates[0] == PlateSizeType.SIZE_TWO_BY_TWO &&
            plates[1] == PlateSizeType.SIZE_THREE_BY_ONE &&
            plates[2] == PlateSizeType.SIZE_ONE_BY_ONE &&
            plates[3] == PlateSizeType.SIZE_ONE_BY_ONE
        -> {
            Plate_2x2_3x1_1x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 2 &&
            plates[0] == PlateSizeType.SIZE_TWO_BY_TWO &&
            plates[1] == PlateSizeType.SIZE_TWO_BY_ONE
        -> {
            Plate_2x2_2x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 &&
            plates[0] == PlateSizeType.SIZE_TWO_BY_TWO &&
            plates[1] == PlateSizeType.SIZE_ONE_BY_ONE &&
            plates[2] == PlateSizeType.SIZE_ONE_BY_ONE
        -> {
            Plate_2x2_1x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 &&
            plates[0] == PlateSizeType.SIZE_THREE_BY_ONE &&
            plates[1] == PlateSizeType.SIZE_THREE_BY_ONE &&
            plates[2] == PlateSizeType.SIZE_THREE_BY_ONE
        -> {
            Plate_3x1_3x1_3x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 4 &&
            plates[0] == PlateSizeType.SIZE_THREE_BY_ONE &&
            plates[1] == PlateSizeType.SIZE_THREE_BY_ONE &&
            plates[2] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[3] == PlateSizeType.SIZE_ONE_BY_ONE
        -> {
            Plate_3x1_3x1_2x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 4 &&
            plates[0] == PlateSizeType.SIZE_THREE_BY_ONE &&
            plates[1] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[2] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[3] == PlateSizeType.SIZE_TWO_BY_ONE
        -> {
            Plate_3x1_2x1_2x1_2x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 5 &&
            plates[0] == PlateSizeType.SIZE_THREE_BY_ONE &&
            plates[1] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[2] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[3] == PlateSizeType.SIZE_ONE_BY_ONE &&
            plates[4] == PlateSizeType.SIZE_ONE_BY_ONE
        -> {
            Plate_3x1_2x1_2x1_1x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 4 &&
            plates[0] == PlateSizeType.SIZE_THREE_BY_ONE &&
            plates[1] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[2] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[3] == PlateSizeType.SIZE_ONE_BY_ONE
        -> {
            Plate_3x1_2x1_2x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 1 &&
            plates[0] == PlateSizeType.SIZE_THREE_BY_ONE
        -> {
            Plate_3x1(
                spendingData = pattern.items[0],
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 2 &&
            plates[0] == PlateSizeType.SIZE_TWO_BY_TWO &&
            plates[1] == PlateSizeType.SIZE_ONE_BY_ONE
        -> {
            Plate_2x2_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 &&
            plates[0] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[1] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[2] == PlateSizeType.SIZE_TWO_BY_ONE
        -> {
            Plate_2x1_2x1_2x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 4 &&
            plates[0] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[1] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[2] == PlateSizeType.SIZE_ONE_BY_ONE &&
            plates[3] == PlateSizeType.SIZE_ONE_BY_ONE
        -> {
            Plate_2x1_2x1_1x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 &&
            plates[0] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[1] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[2] == PlateSizeType.SIZE_ONE_BY_ONE
        -> {
            Plate_2x1_2x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 2 &&
            plates[0] == PlateSizeType.SIZE_TWO_BY_ONE &&
            plates[1] == PlateSizeType.SIZE_ONE_BY_ONE
        -> {
            Plate_2x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 1 &&
            plates[0] == PlateSizeType.SIZE_TWO_BY_ONE
        -> {
            Plate_2x1(
                spendingData = pattern.items[0],
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 &&
            plates[0] == PlateSizeType.SIZE_ONE_BY_ONE &&
            plates[1] == PlateSizeType.SIZE_ONE_BY_ONE &&
            plates[2] == PlateSizeType.SIZE_ONE_BY_ONE
        -> {
            Plate_1x1_1x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 2 &&
            plates[0] == PlateSizeType.SIZE_ONE_BY_ONE &&
            plates[1] == PlateSizeType.SIZE_ONE_BY_ONE
        -> {
            Plate_1x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 1 &&
            plates[0] == PlateSizeType.SIZE_ONE_BY_ONE
        -> {
            Plate_1x1(
                spendingData = pattern.items[0],
                onSpendingClicked = onSpendingClicked,
            )
        }
    }
}

@Composable
fun Plate_3x3(
    spendingData: SpendingData,
    onSpendingClicked: (String) -> Unit,
) {
    SpendingItem(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ONE),
        item = spendingData,
        onSpendingClicked = onSpendingClicked,
    )
}

@Composable
fun Plate_3x2_3x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(TWO_THIRDS),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_3x2_2x1_1x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(TWO_THIRDS),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        Column(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
        ) {
            SpendingItem(
                modifier = Modifier
                    .weight(TWO_THIRDS)
                    .aspectRatio(HALF),
                item = spendingData[1],
                onSpendingClicked = onSpendingClicked,
            )
            SpendingItem(
                modifier = Modifier
                    .weight(ONE_THIRD)
                    .aspectRatio(ONE),
                item = spendingData[2],
                onSpendingClicked = onSpendingClicked,
            )
        }
    }
}

@Composable
fun Plate_2x2_3x1_2x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ONE),
    ) {
        Column(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(TWO_THIRDS),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            SpendingItem(
                modifier = Modifier
                    .weight(TWO_THIRDS)
                    .aspectRatio(ONE),
                item = spendingData[0],
                onSpendingClicked = onSpendingClicked,
            )
            SpendingItem(
                modifier = Modifier
                    .weight(ONE_THIRD)
                    .aspectRatio(TWO),
                item = spendingData[2],
                onSpendingClicked = onSpendingClicked,
            )
        }

        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_2x2_3x1_1x1_1x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ONE),
    ) {
        Column(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(TWO_THIRDS),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            SpendingItem(
                modifier = Modifier
                    .weight(TWO_THIRDS)
                    .aspectRatio(ONE),
                item = spendingData[0],
                onSpendingClicked = onSpendingClicked,
            )
            Row(
                modifier = Modifier
                    .weight(ONE_THIRD),
            ) {
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(ONE),
                    item = spendingData[2],
                    onSpendingClicked = onSpendingClicked,
                )
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(ONE),
                    item = spendingData[3],
                    onSpendingClicked = onSpendingClicked,
                )
            }
        }

        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_2x2_1x1_1x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(ONE),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )

        Column(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            SpendingItem(
                modifier = Modifier
                    .weight(HALF)
                    .aspectRatio(ONE),
                item = spendingData[1],
                onSpendingClicked = onSpendingClicked,
            )
            SpendingItem(
                modifier = Modifier
                    .weight(HALF)
                    .aspectRatio(ONE),
                item = spendingData[2],
                onSpendingClicked = onSpendingClicked,
            )
        }
    }
}

@Composable
fun Plate_2x2_1x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(ONE),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_2x2_2x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(ONE),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )

        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_3x1_3x1_3x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )

        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            item = spendingData[2],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_3x1_3x1_2x1_1x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
        Column(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
        ) {
            SpendingItem(
                modifier = Modifier
                    .weight(TWO_THIRDS)
                    .aspectRatio(HALF),
                item = spendingData[2],
                onSpendingClicked = onSpendingClicked,
            )
            SpendingItem(
                modifier = Modifier
                    .weight(ONE_THIRD)
                    .aspectRatio(ONE),
                item = spendingData[3],
                onSpendingClicked = onSpendingClicked,
            )
        }
    }
}

@Composable
fun Plate_3x1_2x1_2x1_2x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        Column(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(TWO_THIRDS),
        ) {
            Row(
                modifier = Modifier
                    .weight(TWO_THIRDS)
                    .aspectRatio(ONE_AND_TWO_THIRDS),
            ) {
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(HALF),
                    item = spendingData[1],
                    onSpendingClicked = onSpendingClicked,
                )
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(HALF),
                    item = spendingData[2],
                    onSpendingClicked = onSpendingClicked,
                )
            }
            SpendingItem(
                modifier = Modifier
                    .weight(ONE_THIRD)
                    .aspectRatio(TWO),
                item = spendingData[3],
                onSpendingClicked = onSpendingClicked,
            )
        }
    }
}

@Composable
fun Plate_3x1_2x1_2x1_1x1_1x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        Column(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(TWO_THIRDS),
        ) {
            Row(
                modifier = Modifier
                    .weight(TWO_THIRDS)
                    .aspectRatio(ONE_AND_TWO_THIRDS),
            ) {
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(HALF),
                    item = spendingData[1],
                    onSpendingClicked = onSpendingClicked,
                )
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(HALF),
                    item = spendingData[2],
                    onSpendingClicked = onSpendingClicked,
                )
            }
            Row(
                modifier = Modifier
                    .weight(ONE_THIRD)
                    .aspectRatio(TWO),
            ) {
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(ONE),
                    item = spendingData[3],
                    onSpendingClicked = onSpendingClicked,
                )
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(ONE),
                    item = spendingData[4],
                    onSpendingClicked = onSpendingClicked,
                )
            }
        }
    }
}

@Composable
fun Plate_3x1_2x1_2x1_1x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        Column(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(TWO_THIRDS),
        ) {
            Row(
                modifier = Modifier
                    .weight(TWO_THIRDS)
                    .aspectRatio(ONE_AND_TWO_THIRDS),
            ) {
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(HALF),
                    item = spendingData[1],
                    onSpendingClicked = onSpendingClicked,
                )
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(HALF),
                    item = spendingData[2],
                    onSpendingClicked = onSpendingClicked,
                )
            }
            Row(
                modifier = Modifier
                    .weight(ONE_THIRD)
                    .aspectRatio(TWO),
                horizontalArrangement = Arrangement.Start,
            ) {
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(ONE),
                    item = spendingData[3],
                    onSpendingClicked = onSpendingClicked,
                )
                Spacer(
                    modifier = Modifier
                        .weight(HALF),
                )
            }
        }
    }
}

@Composable
fun Plate_2x1_2x1_2x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(ONE_AND_ONE_THIRDS)) {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )

        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            item = spendingData[2],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_2x1_2x1_1x1_1x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(ONE_AND_ONE_THIRDS)) {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
        Column(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
        ) {
            SpendingItem(
                modifier = Modifier
                    .weight(HALF)
                    .aspectRatio(ONE),
                item = spendingData[2],
                onSpendingClicked = onSpendingClicked,
            )
            SpendingItem(
                modifier = Modifier
                    .weight(HALF)
                    .aspectRatio(ONE),
                item = spendingData[3],
                onSpendingClicked = onSpendingClicked,
            )
        }
    }
}

@Composable
fun Plate_2x1_2x1_1x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(ONE_AND_ONE_THIRDS)) {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
        Column(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
        ) {
            SpendingItem(
                modifier = Modifier
                    .weight(HALF)
                    .aspectRatio(ONE),
                item = spendingData[2],
                onSpendingClicked = onSpendingClicked,
            )
            Spacer(
                modifier = Modifier
                    .weight(HALF)
                    .aspectRatio(ONE),
            )
        }
    }
}

@Composable
fun Plate_2x1_1x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(THREE)) {
        SpendingItem(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(TWO),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_1x1_1x1_1x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(THREE)) {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            item = spendingData[2],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_1x1_1x1(
    spendingData: List<SpendingData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(THREE)) {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
        Spacer(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
        )
    }
}

@Composable
fun Plate_1x1(
    spendingData: SpendingData,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(THREE)) {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            item = spendingData,
            onSpendingClicked = onSpendingClicked,
        )
        Spacer(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
        )
        Spacer(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
        )
    }
}

@Composable
fun Plate_3x1(
    spendingData: SpendingData,
    onSpendingClicked: (String) -> Unit,
) {
    SpendingItem(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(THREE),
        item = spendingData,
        onSpendingClicked = onSpendingClicked,
    )
}

@Composable
fun Plate_2x1(
    spendingData: SpendingData,
    onSpendingClicked: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(THREE),
    ) {
        SpendingItem(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(TWO),
            item = spendingData,
            onSpendingClicked = onSpendingClicked,
        )
        Spacer(
            modifier = Modifier
                .weight(ONE_THIRD),
        )
    }
}

@Composable
fun Plate_3x2(
    spendingData: SpendingData,
    onSpendingClicked: (String) -> Unit,
) {
    SpendingItem(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ONE_AND_ONE_THIRDS),
        item = spendingData,
        onSpendingClicked = onSpendingClicked,
    )
}

@Composable
fun SpendingItem(
    modifier: Modifier = Modifier,
    item: SpendingData,
    onSpendingClicked: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .padding(end = 16.dp, bottom = 16.dp)
            .clickable {
                onSpendingClicked(item.id)
            },
        contentAlignment = Alignment.BottomCenter,
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            painter = item.category.iconUri?.let {
                rememberImagePainter(it)
            } ?: item.category.iconId?.let {
                painterResource(
                    id = it,
                )
            } ?: painterResource(R.drawable.icon),
            contentDescription = null,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary
                        .copy(alpha = 0.9f),
                ),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                text = item.name,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_3x3_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_THREE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                            )
                        },
                    ),
                ),
                onSpendingClicked = { },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_3x2_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_TWO,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_3x2_3x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_TWO,
                                PlateSizeType.SIZE_THREE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_3x2_2x1_1x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_TWO,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                                Mock.spendingsList[2],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_2x2_3x1_2x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_TWO,
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                                Mock.spendingsList[2],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_2x2_3x1_1x1_1x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_TWO,
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                                Mock.spendingsList[3],
                                Mock.spendingsList[4],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_2x2_2x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_TWO,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_2x2_1x1_1x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_TWO,
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
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_2x2_1x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_TWO,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_3x1_3x1_3x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_THREE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                                Mock.spendingsList[2],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_3x1_3x1_2x1_1x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                                Mock.spendingsList[2],
                                Mock.spendingsList[3],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_3x1_2x1_2x1_2x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                                Mock.spendingsList[2],
                                Mock.spendingsList[3],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_3x1_2x1_2x1_1x1_1x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                                Mock.spendingsList[2],
                                Mock.spendingsList[3],
                                Mock.spendingsList[4],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_3x1_2x1_2x1_1x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                                Mock.spendingsList[2],
                                Mock.spendingsList[3],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_2x1_2x1_2x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                                Mock.spendingsList[2],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_2x1_2x1_1x1_1x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                                Mock.spendingsList[2],
                                Mock.spendingsList[3],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_2x1_2x1_1x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                                Mock.spendingsList[2],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_3x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
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
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_2x1_1x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    listOf(
                        Pattern<SpendingData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                            )
                        },
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Plate_1x1_1x1_1x1_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
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
                onSpendingClicked = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlateWithDuplicatesAndDatesPreview() {
    val sorter = PlatesSorter<SpendingData>()
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                listOf(
                    sorter.findPattern(
                        sorter.preparePlatesSizes(
                            listOf(
                                Mock.spendingsList[0],
                                Mock.spendingsList[1],
                                Mock.spendingsList[2],
                                Mock.spendingsList[3],
                                Mock.spendingsList[4],
                                Mock.spendingsList[5],
                            ),
                        ),
                    ),
                    sorter.findPattern(
                        sorter.preparePlatesSizes(
                            listOf(
                                Mock.spendingsList[6],
                            ),
                        ),
                    ),
                ),
                onSpendingClicked = {},
            )
        }
    }
}
