@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.familybudget.ui.family

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.BaseTextField
import com.faigenbloom.familybudget.common.StripeBar
import com.faigenbloom.familybudget.common.TopBar
import com.faigenbloom.familybudget.common.coders.painterQR
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme

@Composable
fun FamilyPage(
    state: FamilyState,
    onQRScanRequested: () -> Unit,
    onBack: () -> Unit,
) {
    Box {
        Column {
            TopBar(
                endIcon = R.drawable.icon_qrcode,
                startIcon = R.drawable.icon_arrow_back,
                onEndIconCLicked = { state.onQRVisibilityChanged(true) },
                preEndIcon = R.drawable.icon_qrcode_scan,
                onPreEndIconCLicked = { onQRScanRequested() },
                onStartIconCLicked = onBack,
            )
            StripeBar(
                textId = R.string.settings_family,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                colorScheme.secondary,
                                colorScheme.secondaryContainer,
                            ),
                            center = Offset(1080f, 100f),
                        ),
                    ),
                contentAlignment = Alignment.BottomStart,
            ) {
                BaseTextField(
                    text = state.familyName,
                    textColor = colorScheme.onSecondary,
                    labelId = R.string.family_name,
                    onTextChange = state.onFamilyNameChanged,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color = colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp,
                        ),
                    text = stringResource(R.string.settings_personal),
                    color = colorScheme.onPrimary,
                    style = typography.titleMedium,
                )
            }

            LazyColumn {
                items(state.familyList.size) {
                    Row(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .padding(
                                vertical = 8.dp,
                                horizontal = 16.dp,
                            )
                            .background(
                                color = colorScheme.secondary,
                                shape = shapes.small,
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                            text = state.familyList[it],
                            color = colorScheme.onPrimary,
                            style = typography.titleMedium,
                        )
                        Image(
                            modifier = Modifier
                                .size(32.dp)
                                .padding(horizontal = 8.dp)
                                .aspectRatio(1f)
                                .clickable {
                                    state.onDeleteFamilyMember(it)
                                },
                            painter = painterResource(id = R.drawable.icon_delete),
                            contentDescription = "",
                        )
                    }
                }
            }
        }
        if (state.isQRDialogOpened) {
            AlertDialog(
                modifier = Modifier.fillMaxSize(),
                onDismissRequest = {
                    state.onQRVisibilityChanged(false)
                },
            ) {
                Image(
                    painter = painterQR(
                        text = state.familyId,
                        backColor = colorScheme.primary,
                        frontColor = colorScheme.onBackground,
                    ),
                    contentDescription = "",
                )
            }
        }
    }
}

@Preview
@Composable
fun StatisticsPagePreview() {
    FamillySpandingsTheme {
        Surface {
            FamilyPage(
                state = FamilyState(
                    familyId = "asdgfasdg",
                    familyName = "Zakharchenko",
                    familyList = listOf(
                        "Konstantyn Zakharchenko",
                        "Victoria Zakharchenko",
                        "Natalia Zakharchenko",
                    ),
                    isQRDialogOpened = false,
                    onQRVisibilityChanged = {},
                    onQrScanned = {},
                    onSave = {},
                    onFamilyNameChanged = {},
                    onDeleteFamilyMember = {},
                ),
                onQRScanRequested = {},
                onBack = {},
            )
        }
    }
}
