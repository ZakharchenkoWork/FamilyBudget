@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.family

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.comon.BaseTextField
import com.faigenbloom.famillyspandings.comon.StripeBar
import com.faigenbloom.famillyspandings.comon.TopBar
import com.faigenbloom.famillyspandings.comon.painterQR
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun FamilyPage(state: FamilyState) {
    Box {
        Column {
            TopBar(
                endIcon = R.drawable.icon_qrcode,
                onEndIconCLicked = { state.onQRVisibilityChanged(true) },
                preEndIcon = R.drawable.icon_qrcode_scan,
                onPreEndIconCLicked = { state.onQRVisibilityChanged(true) },
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
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.secondaryContainer,
                            ),
                            center = Offset(1080f, 100f),
                        ),
                    ),
                contentAlignment = Alignment.BottomStart,
            ) {
                BaseTextField(
                    text = "",
                    labelId = R.string.family_name,
                    onTextChange = {
                    },
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color = MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp,
                        ),
                    text = stringResource(R.string.settings_personal),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            LazyColumn {
                items(state.familyList.size) {
                    Text(
                        modifier = Modifier
                            .padding(
                                vertical = 8.dp,
                                horizontal = 16.dp,
                            ),
                        text = state.familyList[it],
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                    )
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
                        backColor = MaterialTheme.colorScheme.primary,
                        frontColor = MaterialTheme.colorScheme.onBackground,
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
                    name = "Zakharchenko",
                    familyList = listOf(
                        "Konstantyn Zakharchenko",
                        "Victoria Zakharchenko",
                        "Natalia Zakharchenko",
                    ),
                    isQRDialogOpened = true,
                    onQRVisibilityChanged = {},
                ),
            )
        }
    }
}
