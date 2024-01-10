package com.faigenbloom.famillyspandings.spandings.show

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.comon.TopBar
import com.faigenbloom.famillyspandings.spandings.CategoriesMock
import com.faigenbloom.famillyspandings.spandings.edit.SpendingDetail
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun SpendingShowPage(state: SpendingShowState, onEditClicked: (String) -> Unit) {
    Column {
        TopBar(
            title = "",
            preEndIcon = R.drawable.icon_duplicate,
            onPreEndIconCLicked = { state.onDuplicateClicked(onEditClicked) },
            endIcon = R.drawable.pen,
            onEndIconCLicked = { onEditClicked(state.id) },
        )
        ConstraintLayout {
            val (topStripe, info, bottomStripe) = createRefs()

            Stripe(
                modifier = Modifier
                    .constrainAs(topStripe) {
                        top.linkTo(parent.top, margin = 32.dp)
                    }
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                    ),
                text = state.name,
                textColor = MaterialTheme.colorScheme.tertiary,
            )
            Information(
                modifier = Modifier
                    .constrainAs(info) {
                        top.linkTo(topStripe.top, margin = 8.dp)
                    }
                    .padding(bottom = 32.dp),
                state = state,
            )
            Stripe(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    )
                    .constrainAs(bottomStripe) {
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    },
                text = state.amount,
                textColor = MaterialTheme.colorScheme.onPrimary,
                isPlanned = state.isPlanned,
                onMarkPurchasedClicked = state.onMarkPurchasedClicked,
            )
        }
        DatailsList(state.details)
    }
}

@Composable
fun Stripe(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color,
    isPlanned: Boolean = false,
    onMarkPurchasedClicked: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        if (isPlanned) {
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .clickable {
                        onMarkPurchasedClicked?.invoke()
                    }
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                    ),
            ) {
                Text(
                    modifier = Modifier
                        .padding(all = 8.dp),
                    text = stringResource(id = R.string.spending_mark_purchased),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        } else {
            Spacer(
                modifier = Modifier
                    .weight(0.5f),
            )
        }
        Text(
            modifier = Modifier
                .weight(0.5f)
                .padding(vertical = 8.dp),
            text = text,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            color = textColor,
        )
    }
}

@Composable
fun Information(
    modifier: Modifier = Modifier,
    state: SpendingShowState,
) {
    Box(modifier = modifier) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .wrapContentSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        modifier = Modifier
                            .size(170.dp)
                            .clip(CircleShape),
                        painter = state.photoUri?.let {
                            rememberImagePainter(it) {
                                transformations(CircleCropTransformation())
                            }
                        } ?: state.category.iconId?.let {
                            painterResource(state.category.iconId)
                        } ?: state.category.iconUri?.let {
                            rememberImagePainter(it) {
                                transformations(CircleCropTransformation())
                            }
                        } ?: painterResource(id = R.drawable.photo),
                        contentDescription = "",
                    )
                }
                Column(
                    modifier = Modifier.weight(0.5f),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.category),
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    val categoryName = state.category.nameId?.let {
                        stringResource(id = it)
                    } ?: state.category.name ?: ""

                    Text(
                        text = categoryName,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = state.date,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
    }
}

@Composable
fun DatailsList(detailsList: List<SpendingDetail>) {
    LazyColumn {
        items(detailsList.size) { detailIndex ->
            DetailsItem(
                spendingDetail = detailsList[detailIndex],
            )
        }
    }
}

@Composable
fun DetailsItem(
    spendingDetail: SpendingDetail,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(horizontal = 8.dp),
                color = MaterialTheme.colorScheme.onBackground,
                text = spendingDetail.name,
            )
            Text(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onBackground,
                text = spendingDetail.amount,
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.secondary),
        )
    }
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun SpendingEditPageDetailsPreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            SpendingShowPage(
                state = SpendingShowState(
                    id = "asdfasd",
                    name = "Home",
                    amount = "10.00",
                    date = "01.11.2023",
                    category = CategoriesMock.categoriesList[1],
                    photoUri = null,
                    isPlanned = true,
                    details = listOf(
                        SpendingDetail("asdfasd", "Food", "400"),
                        SpendingDetail("asdfasdasd", "Shampoo", "35"),
                        SpendingDetail("asdfasddddd", "Drops", "50"),
                    ),
                    onMarkPurchasedClicked = { },
                    onDuplicateClicked = {},
                ),
                onEditClicked = {},
            )
        }
    }
}
