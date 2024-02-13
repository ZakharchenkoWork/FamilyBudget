@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.faigenbloom.familybudget.ui.spendings.list

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.toReadableDate
import com.faigenbloom.familybudget.common.toReadableMonth
import com.faigenbloom.familybudget.common.toReadableYear
import com.faigenbloom.familybudget.common.ui.LoadingIndicator
import com.faigenbloom.familybudget.domain.spendings.DatedList
import com.faigenbloom.familybudget.domain.spendings.FilterType
import com.faigenbloom.familybudget.domain.spendings.Pattern
import com.faigenbloom.familybudget.domain.spendings.PlateSizeType
import com.faigenbloom.familybudget.ui.categories.getCategoryIcon
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme
import com.faigenbloom.familybudget.ui.theme.hint
import com.faigenbloom.familybudget.ui.theme.transparent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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
    datedPatterns: LazyPagingItems<DatedList>,
    onSpendingClicked: (String) -> Unit,
    filterType: FilterType,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        if (datedPatterns.loadState.prepend is LoadState.Loading) {
            item { LoadingIndicator() }
        }
        repeat(datedPatterns.itemCount) { dateIndex ->
            datedPatterns.peek(dateIndex)?.let { datedPattern ->
                stickyHeader(key = datedPattern.date.toReadableDate()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .background(MaterialTheme.colorScheme.secondary),
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text =
                            when (filterType) {
                                is FilterType.Daily -> datedPattern.date.toReadableDate()
                                is FilterType.Monthly -> datedPattern.date.toReadableMonth()
                                is FilterType.Yearly -> datedPattern.date.toReadableYear()
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }

                items(datedPattern.patterns.size) { patternIndex ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                    ) {
                        GetPlates(
                            pattern = datedPattern[patternIndex],
                            onSpendingClicked = onSpendingClicked,
                        )
                    }
                    if (patternIndex == datedPattern.patterns.size - 1) {
                        datedPatterns[dateIndex]
                    }
                }
            }
        }

        if (datedPatterns.loadState.refresh is LoadState.Loading) {
            item { LoadingIndicator() }
        }
        if (datedPatterns.loadState.append is LoadState.Loading) {
            item { LoadingIndicator() }
        }
    }
}

@Composable
private fun GetPlates(
    pattern: Pattern<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    val plates = pattern.plates
    when {
        plates.size == 1 && plates[0] == PlateSizeType.SIZE_THREE_BY_THREE -> {
            Plate_3x3(
                spendingData = pattern.items[0],
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 2 && plates[0] == PlateSizeType.SIZE_THREE_BY_TWO && plates[1] == PlateSizeType.SIZE_THREE_BY_ONE

        -> {
            Plate_3x2_3x1(
                pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 && plates[0] == PlateSizeType.SIZE_THREE_BY_TWO && plates[1] == PlateSizeType.SIZE_TWO_BY_ONE && plates[2] == PlateSizeType.SIZE_ONE_BY_ONE

        -> {
            Plate_3x2_2x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 1 && plates[0] == PlateSizeType.SIZE_THREE_BY_TWO -> {
            Plate_3x2(
                spendingData = pattern.items[0],
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 && plates[0] == PlateSizeType.SIZE_TWO_BY_TWO && plates[1] == PlateSizeType.SIZE_THREE_BY_ONE && plates[2] == PlateSizeType.SIZE_TWO_BY_ONE -> {
            Plate_2x2_3x1_2x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 4 && plates[0] == PlateSizeType.SIZE_TWO_BY_TWO && plates[1] == PlateSizeType.SIZE_THREE_BY_ONE && plates[2] == PlateSizeType.SIZE_ONE_BY_ONE && plates[3] == PlateSizeType.SIZE_ONE_BY_ONE -> {
            Plate_2x2_3x1_1x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 2 && plates[0] == PlateSizeType.SIZE_TWO_BY_TWO && plates[1] == PlateSizeType.SIZE_TWO_BY_ONE -> {
            Plate_2x2_2x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 && plates[0] == PlateSizeType.SIZE_TWO_BY_TWO && plates[1] == PlateSizeType.SIZE_ONE_BY_ONE && plates[2] == PlateSizeType.SIZE_ONE_BY_ONE -> {
            Plate_2x2_1x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 && plates[0] == PlateSizeType.SIZE_THREE_BY_ONE && plates[1] == PlateSizeType.SIZE_THREE_BY_ONE && plates[2] == PlateSizeType.SIZE_THREE_BY_ONE -> {
            Plate_3x1_3x1_3x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 4 && plates[0] == PlateSizeType.SIZE_THREE_BY_ONE && plates[1] == PlateSizeType.SIZE_THREE_BY_ONE && plates[2] == PlateSizeType.SIZE_TWO_BY_ONE && plates[3] == PlateSizeType.SIZE_ONE_BY_ONE -> {
            Plate_3x1_3x1_2x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 4 && plates[0] == PlateSizeType.SIZE_THREE_BY_ONE && plates[1] == PlateSizeType.SIZE_TWO_BY_ONE && plates[2] == PlateSizeType.SIZE_TWO_BY_ONE && plates[3] == PlateSizeType.SIZE_TWO_BY_ONE -> {
            Plate_3x1_2x1_2x1_2x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 5 && plates[0] == PlateSizeType.SIZE_THREE_BY_ONE && plates[1] == PlateSizeType.SIZE_TWO_BY_ONE && plates[2] == PlateSizeType.SIZE_TWO_BY_ONE && plates[3] == PlateSizeType.SIZE_ONE_BY_ONE && plates[4] == PlateSizeType.SIZE_ONE_BY_ONE -> {
            Plate_3x1_2x1_2x1_1x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 4 && plates[0] == PlateSizeType.SIZE_THREE_BY_ONE && plates[1] == PlateSizeType.SIZE_TWO_BY_ONE && plates[2] == PlateSizeType.SIZE_TWO_BY_ONE && plates[3] == PlateSizeType.SIZE_ONE_BY_ONE -> {
            Plate_3x1_2x1_2x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 1 && plates[0] == PlateSizeType.SIZE_THREE_BY_ONE -> {
            Plate_3x1(
                spendingData = pattern.items[0],
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 2 && plates[0] == PlateSizeType.SIZE_TWO_BY_TWO && plates[1] == PlateSizeType.SIZE_ONE_BY_ONE -> {
            Plate_2x2_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 && plates[0] == PlateSizeType.SIZE_TWO_BY_ONE && plates[1] == PlateSizeType.SIZE_TWO_BY_ONE && plates[2] == PlateSizeType.SIZE_TWO_BY_ONE -> {
            Plate_2x1_2x1_2x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 4 && plates[0] == PlateSizeType.SIZE_TWO_BY_ONE && plates[1] == PlateSizeType.SIZE_TWO_BY_ONE && plates[2] == PlateSizeType.SIZE_ONE_BY_ONE && plates[3] == PlateSizeType.SIZE_ONE_BY_ONE -> {
            Plate_2x1_2x1_1x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 && plates[0] == PlateSizeType.SIZE_TWO_BY_ONE && plates[1] == PlateSizeType.SIZE_TWO_BY_ONE && plates[2] == PlateSizeType.SIZE_ONE_BY_ONE -> {
            Plate_2x1_2x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 2 && plates[0] == PlateSizeType.SIZE_TWO_BY_ONE && plates[1] == PlateSizeType.SIZE_ONE_BY_ONE -> {
            Plate_2x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 1 && plates[0] == PlateSizeType.SIZE_TWO_BY_ONE -> {
            Plate_2x1(
                spendingData = pattern.items[0],
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 3 && plates[0] == PlateSizeType.SIZE_ONE_BY_ONE && plates[1] == PlateSizeType.SIZE_ONE_BY_ONE && plates[2] == PlateSizeType.SIZE_ONE_BY_ONE -> {
            Plate_1x1_1x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 2 && plates[0] == PlateSizeType.SIZE_ONE_BY_ONE && plates[1] == PlateSizeType.SIZE_ONE_BY_ONE -> {
            Plate_1x1_1x1(
                spendingData = pattern.items,
                onSpendingClicked = onSpendingClicked,
            )
        }

        plates.size == 1 && plates[0] == PlateSizeType.SIZE_ONE_BY_ONE -> {
            Plate_1x1(
                spendingData = pattern.items[0],
                onSpendingClicked = onSpendingClicked,
            )
        }
    }
}

@Composable
fun Plate_3x3(
    spendingData: SpendingCategoryUiData,
    onSpendingClicked: (String) -> Unit,
) {
    SpendingItem(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ONE),
        isSmall = false,
        item = spendingData,
        onSpendingClicked = onSpendingClicked,
    )
}

@Composable
fun Plate_3x2_3x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(TWO_THIRDS),
            isSmall = false,
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            isSmall = false,
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_3x2_2x1_1x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(TWO_THIRDS),
            isSmall = false,
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
                isSmall = false,
                item = spendingData[1],
                onSpendingClicked = onSpendingClicked,
            )
            SpendingItem(
                modifier = Modifier
                    .weight(ONE_THIRD)
                    .aspectRatio(ONE),
                isSmall = true,
                item = spendingData[2],
                onSpendingClicked = onSpendingClicked,
            )
        }
    }
}

@Composable
fun Plate_2x2_3x1_2x1(
    spendingData: List<SpendingCategoryUiData>,
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
                isSmall = false,
                item = spendingData[0],
                onSpendingClicked = onSpendingClicked,
            )
            SpendingItem(
                modifier = Modifier
                    .weight(ONE_THIRD)
                    .aspectRatio(TWO),
                isSmall = false,
                item = spendingData[2],
                onSpendingClicked = onSpendingClicked,
            )
        }

        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            isSmall = false,
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_2x2_3x1_1x1_1x1(
    spendingData: List<SpendingCategoryUiData>,
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
                isSmall = false,
                item = spendingData[0],
                onSpendingClicked = onSpendingClicked,
            )
            Row(
                modifier = Modifier.weight(ONE_THIRD),
            ) {
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(ONE),
                    isSmall = true,
                    item = spendingData[2],
                    onSpendingClicked = onSpendingClicked,
                )
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(ONE),
                    isSmall = true,
                    item = spendingData[3],
                    onSpendingClicked = onSpendingClicked,
                )
            }
        }

        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            isSmall = false,
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_2x2_1x1_1x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(ONE),
            isSmall = false,
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
                isSmall = true,
                item = spendingData[1],
                onSpendingClicked = onSpendingClicked,
            )
            SpendingItem(
                modifier = Modifier
                    .weight(HALF)
                    .aspectRatio(ONE),
                isSmall = true,
                item = spendingData[2],
                onSpendingClicked = onSpendingClicked,
            )
        }
    }
}

@Composable
fun Plate_2x2_1x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(ONE),
            isSmall = false,
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            isSmall = true,
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_2x2_2x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(ONE),
            isSmall = false,
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )

        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            isSmall = false,
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_3x1_3x1_3x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            isSmall = false,
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            isSmall = false,
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )

        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            isSmall = false,
            item = spendingData[2],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_3x1_3x1_2x1_1x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            isSmall = false,
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            isSmall = false,
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
                isSmall = false,
                item = spendingData[2],
                onSpendingClicked = onSpendingClicked,
            )
            SpendingItem(
                modifier = Modifier
                    .weight(ONE_THIRD)
                    .aspectRatio(ONE),
                isSmall = true,
                item = spendingData[3],
                onSpendingClicked = onSpendingClicked,
            )
        }
    }
}

@Composable
fun Plate_3x1_2x1_2x1_2x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            isSmall = false,
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
                    isSmall = false,
                    item = spendingData[1],
                    onSpendingClicked = onSpendingClicked,
                )
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(HALF),
                    isSmall = false,
                    item = spendingData[2],
                    onSpendingClicked = onSpendingClicked,
                )
            }
            SpendingItem(
                modifier = Modifier
                    .weight(ONE_THIRD)
                    .aspectRatio(TWO),
                isSmall = false,
                item = spendingData[3],
                onSpendingClicked = onSpendingClicked,
            )
        }
    }
}

@Composable
fun Plate_3x1_2x1_2x1_1x1_1x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            isSmall = false,
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
                    isSmall = false,
                    item = spendingData[1],
                    onSpendingClicked = onSpendingClicked,
                )
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(HALF),
                    isSmall = false,
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
                    isSmall = true,
                    item = spendingData[3],
                    onSpendingClicked = onSpendingClicked,
                )
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(ONE),
                    isSmall = true,
                    item = spendingData[4],
                    onSpendingClicked = onSpendingClicked,
                )
            }
        }
    }
}

@Composable
fun Plate_3x1_2x1_2x1_1x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE_THIRD),
            isSmall = false,
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
                    isSmall = false,
                    item = spendingData[1],
                    onSpendingClicked = onSpendingClicked,
                )
                SpendingItem(
                    modifier = Modifier
                        .weight(HALF)
                        .aspectRatio(HALF),
                    isSmall = false,
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
                    isSmall = true,
                    onSpendingClicked = onSpendingClicked,
                )
                Spacer(
                    modifier = Modifier.weight(HALF),
                )
            }
        }
    }
}

@Composable
fun Plate_2x1_2x1_2x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(ONE_AND_ONE_THIRDS)) {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            isSmall = false,
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            isSmall = false,
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )

        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            isSmall = false,
            item = spendingData[2],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_2x1_2x1_1x1_1x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(ONE_AND_ONE_THIRDS)) {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            isSmall = false,
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            isSmall = false,
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
                isSmall = true,
                item = spendingData[2],
                onSpendingClicked = onSpendingClicked,
            )
            SpendingItem(
                modifier = Modifier
                    .weight(HALF)
                    .aspectRatio(ONE),
                isSmall = true,
                item = spendingData[3],
                onSpendingClicked = onSpendingClicked,
            )
        }
    }
}

@Composable
fun Plate_2x1_2x1_1x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(ONE_AND_ONE_THIRDS)) {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            isSmall = false,
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(HALF),
            isSmall = false,
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
                isSmall = true,
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
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(THREE)) {
        SpendingItem(
            modifier = Modifier
                .weight(TWO_THIRDS)
                .aspectRatio(TWO),
            isSmall = false,
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            isSmall = true,
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_1x1_1x1_1x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(THREE)) {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            isSmall = true,
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            isSmall = true,
            item = spendingData[1],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            isSmall = true,
            item = spendingData[2],
            onSpendingClicked = onSpendingClicked,
        )
    }
}

@Composable
fun Plate_1x1_1x1(
    spendingData: List<SpendingCategoryUiData>,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(THREE)) {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            isSmall = true,
            item = spendingData[0],
            onSpendingClicked = onSpendingClicked,
        )
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            isSmall = true,
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
    spendingData: SpendingCategoryUiData,
    onSpendingClicked: (String) -> Unit,
) {
    Row(modifier = Modifier.aspectRatio(THREE)) {
        SpendingItem(
            modifier = Modifier
                .weight(ONE_THIRD)
                .aspectRatio(ONE),
            isSmall = true,
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
    spendingData: SpendingCategoryUiData,
    onSpendingClicked: (String) -> Unit,
) {
    SpendingItem(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(THREE),
        item = spendingData,
        isSmall = false,
        onSpendingClicked = onSpendingClicked,
    )
}

@Composable
fun Plate_2x1(
    spendingData: SpendingCategoryUiData,
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
            isSmall = false,
            onSpendingClicked = onSpendingClicked,
        )
        Spacer(
            modifier = Modifier.weight(ONE_THIRD),
        )
    }
}

@Composable
fun Plate_3x2(
    spendingData: SpendingCategoryUiData,
    onSpendingClicked: (String) -> Unit,
) {
    SpendingItem(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ONE_AND_ONE_THIRDS),
        item = spendingData,
        isSmall = false,
        onSpendingClicked = onSpendingClicked,
    )
}

@Composable
fun SpendingItem(
    modifier: Modifier = Modifier,
    item: SpendingCategoryUiData,
    isSmall: Boolean,
    onSpendingClicked: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .padding(end = 16.dp, bottom = 16.dp)
            .clickable {
                onSpendingClicked(item.id)
            },
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = Crop,
            painter = item.photoUri?.let {
                rememberImagePainter(it)
            } ?: item.category.getCategoryIcon(isSmall),
            contentDescription = null,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (item.isHidden) {
                        MaterialTheme.colorScheme
                            .hint()
                            .copy(alpha = 0.5f)
                    } else {
                        MaterialTheme.colorScheme.transparent()
                    },
                ),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom,
        ) {
            if (item.isHidden) {
                Image(
                    modifier = Modifier
                        .padding(
                            horizontal = 8.dp,
                            vertical = 8.dp,
                        )
                        .size(32.dp),
                    painter = painterResource(R.drawable.icon_hidden),
                    contentDescription = null,
                )
            }
            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                    )
                    .fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = item.name,
                )
            }
        }
    }
}

fun asPage(patterns: List<Pattern<SpendingCategoryUiData>>): Flow<PagingData<DatedList>> {
    return flowOf(
        value = PagingData.from(
            listOf(
                DatedList(
                    patterns,
                ),
            ),
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun Plate_3x3_Preview() {
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_THREE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = { },
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_TWO,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_TWO,
                                PlateSizeType.SIZE_THREE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_TWO,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                                mockSpendingsWithCategoryList[2],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_TWO,
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                                mockSpendingsWithCategoryList[2],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_TWO,
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                                mockSpendingsWithCategoryList[3],
                                mockSpendingsWithCategoryList[4],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_TWO,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_TWO,
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
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_TWO,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_THREE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                                mockSpendingsWithCategoryList[2],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                                mockSpendingsWithCategoryList[2],
                                mockSpendingsWithCategoryList[3],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                                mockSpendingsWithCategoryList[2],
                                mockSpendingsWithCategoryList[3],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                                mockSpendingsWithCategoryList[2],
                                mockSpendingsWithCategoryList[3],
                                mockSpendingsWithCategoryList[4],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_THREE_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                                mockSpendingsWithCategoryList[2],
                                mockSpendingsWithCategoryList[3],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                                mockSpendingsWithCategoryList[2],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                                mockSpendingsWithCategoryList[2],
                                mockSpendingsWithCategoryList[3],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                                mockSpendingsWithCategoryList[2],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
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
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
                    listOf(
                        Pattern<SpendingCategoryUiData>(
                            listOf(
                                PlateSizeType.SIZE_TWO_BY_ONE,
                                PlateSizeType.SIZE_ONE_BY_ONE,
                            ),
                        ).apply {
                            items = listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                            )
                        },
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
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
                asPage(
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
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
            )
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun PlateWithDuplicatesAndDatesPreview() {
    val sorter = SortPlatesUseCase<SpendingCategoryUiData>()
    FamillySpandingsTheme {
        Surface {
            DynamicPlatesHolder(
                asPage(
                    sorter.findPattern(
                        sorter.preparePlatesSizes(
                            listOf(
                                mockSpendingsWithCategoryList[0],
                                mockSpendingsWithCategoryList[1],
                                mockSpendingsWithCategoryList[2],
                                mockSpendingsWithCategoryList[3],
                                mockSpendingsWithCategoryList[4],
                                mockSpendingsWithCategoryList[5],
                            ),
                        ),
                    ),
                    sorter.findPattern(
                        sorter.preparePlatesSizes(
                            listOf(
                                mockSpendingsWithCategoryList[6],
                            ),
                        ),
                    ),
                ).collectAsLazyPagingItems(),
                onSpendingClicked = {},
                filterType = FilterType.Daily(),
            )
        }
    }
}
*/
