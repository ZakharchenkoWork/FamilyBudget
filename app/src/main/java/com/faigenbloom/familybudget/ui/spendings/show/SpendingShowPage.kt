package com.faigenbloom.familybudget.ui.spendings.show

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.TopBar
import com.faigenbloom.familybudget.common.ui.MoneyTextTransformation
import com.faigenbloom.familybudget.ui.categories.getCategoryIcon
import com.faigenbloom.familybudget.ui.categories.getCategoryName
import com.faigenbloom.familybudget.ui.categories.mockCategoriesList
import com.faigenbloom.familybudget.ui.spendings.DetailUiData
import com.faigenbloom.familybudget.ui.spendings.edit.mockDetailsList
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme
import java.util.Currency
import java.util.Locale

@Composable
fun SpendingShowPage(
    state: SpendingShowState,
    onBack: () -> Unit,
) {
    Column {
        TopBar(
            title = if (state.isCurrentUserOwner) {
                stringResource(R.string.spending_showing_title_this_user)
            } else {
                stringResource(R.string.spending_showing_title_another_user, state.ownerName)
            },
            startIcon = R.drawable.icon_arrow_back,
            onStartIconCLicked = onBack,
        )
        ConstraintLayout {
            val (topStripe, info, bottomStripe) = createRefs()

            TopStripe(
                modifier = Modifier
                    .constrainAs(topStripe) {
                        top.linkTo(parent.top, margin = 32.dp)
                    },
                text = state.name,
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
                    .constrainAs(bottomStripe) {
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    },
                state = state,
            )
        }
        DetailsList(
            detailsList = state.details,
            currencyCode = state.currency.currencyCode,
        )
    }
}


@Composable
fun TopStripe(
    modifier: Modifier = Modifier,
    text: String,
) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
            )
            .fillMaxWidth(),
    ) {
        Spacer(
            modifier = Modifier
                .weight(0.5f),
        )
        Text(
            modifier = Modifier
                .weight(0.5f)
                .padding(vertical = 8.dp),
            text = text,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Composable
fun Stripe(
    modifier: Modifier = Modifier,
    state: SpendingShowState,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            ),
    ) {

        Row(
            modifier = Modifier
                .weight(0.5f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (state.isHidden) {
                Image(
                    modifier = Modifier
                        .height(32.dp)
                        .aspectRatio(1f)
                        .padding(start = 16.dp),
                    painter = painterResource(id = R.drawable.icon_hidden),
                    contentDescription = "",
                )
            }
            if (state.isPlanned) {
                Image(
                    modifier = Modifier
                        .height(32.dp)
                        .aspectRatio(1f)
                        .padding(start = 16.dp),
                    painter = painterResource(id = R.drawable.icon_list_planned_outlined),
                    contentDescription = "",
                )
            }
        }
        Text(
            modifier = Modifier
                .weight(0.5f)
                .padding(vertical = 8.dp),
            text = MoneyTextTransformation(state.currency.currencyCode)
                .filter(state.amount),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
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
                        } ?: state.category.getCategoryIcon(isCircle = true),
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
                    Text(
                        text = state.category.getCategoryName(),
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
fun DetailsList(
    detailsList: List<DetailUiData>,
    currencyCode: String,
) {
    LazyColumn {
        items(detailsList.size) { detailIndex ->
            DetailsItem(
                spendingDetail = detailsList[detailIndex],
                currencyCode = currencyCode,
            )
        }
    }
}

@Composable
fun DetailsItem(
    spendingDetail: DetailUiData,
    currencyCode: String,
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
                text = MoneyTextTransformation(currencyCode)
                    .filter(spendingDetail.amount),
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
                    name = "Home",
                    amount = "10.00",
                    date = "01.11.2023",
                    currency = Currency.getInstance(Locale.getDefault()),
                    category = mockCategoriesList[1],
                    photoUri = null,
                    isPlanned = true,
                    isHidden = true,
                    details = mockDetailsList,
                    onMarkPurchasedClicked = { },
                    onDuplicateClicked = {},
                    onEditClicked = {},
                ),
                onBack = {},
            )
        }
    }
}
