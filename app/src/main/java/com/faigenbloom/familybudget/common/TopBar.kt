package com.faigenbloom.familybudget.common

import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme

const val BACK_BUTTON = "BACK_BUTTON"
const val LEFT_TOP_BAR_BUTTON = "LEFT_TOP_BAR_BUTTON"
const val RIGHT_TOP_BAR_BUTTON = "RIGHT_TOP_BAR_BUTTON"

@Composable
fun TopBar(
    title: String = "",
    startIcon: Int? = null,
    onStartIconCLicked: (() -> Unit)? = null,
    preEndIcon: Int? = null,
    onPreEndIconCLicked: (() -> Unit)? = null,
    endIcon: Int? = null,
    onEndIconCLicked: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            startIcon?.let {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onStartIconCLicked?.invoke()
                        }
                        .semantics {
                            contentDescription = BACK_BUTTON
                        },
                    painter = painterResource(id = it),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary,
                )
            } ?: Spacer(modifier = Modifier.size(24.dp))
            Row(
                modifier = Modifier.wrapContentWidth(),
            ) {
                preEndIcon?.let { icon ->
                    Image(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(24.dp)
                            .clickable {
                                onPreEndIconCLicked?.invoke()
                            },
                        painter = painterResource(id = icon),
                        contentDescription = "",
                    )
                }
                endIcon?.let { icon ->
                    Image(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                onEndIconCLicked?.invoke()
                            },
                        painter = painterResource(id = icon),
                        contentDescription = "",
                    )
                }
            }
        }
        if (title.isBlank()) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .padding(vertical = 4.dp),
            )
        } else {
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
fun StripeBar(
    @StringRes textId: Int,
    secondTabTextId: Int? = null,
    isLeftSelected: Boolean = true,
    onSelectionChanged: ((Boolean) -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .weight(0.5f)
                .clickable { onSelectionChanged?.invoke(true) }
                .semantics { contentDescription = LEFT_TOP_BAR_BUTTON }
                .background(
                    color = if (isLeftSelected) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.secondaryContainer
                    },
                ),
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = stringResource(id = textId),
                style = MaterialTheme.typography.titleMedium,
                color = if (isLeftSelected) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.onSecondaryContainer
                },
            )
        }
        secondTabTextId?.let {
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .clickable { onSelectionChanged?.invoke(false) }
                    .semantics {
                        contentDescription = RIGHT_TOP_BAR_BUTTON
                    }
                    .background(
                        color = if (isLeftSelected) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.secondary
                        },
                    ),

                ) {
                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = stringResource(id = it),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isLeftSelected) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun TopBarPreview() {
    FamillySpandingsTheme {
        Column(modifier = Modifier.background(Color.White)) {
            TopBar(
                title = "Auth",
                startIcon = R.drawable.icon_arrow_back,
                endIcon = R.drawable.icon_edit,
                preEndIcon = R.drawable.icon_edit,
                onStartIconCLicked = {},
            )
            StripeBar(R.string.authorization)
        }
    }
}

@Preview
@Composable
fun TopBarDoublePreview() {
    FamillySpandingsTheme {
        Column(modifier = Modifier.background(Color.White)) {
            TopBar(
                title = "Auth",
                startIcon = R.drawable.icon_arrow_back,
                onStartIconCLicked = {},
            )
            StripeBar(
                textId = R.string.authorization,
                secondTabTextId = R.string.registration,
                isLeftSelected = false,
            )
        }
    }
}
