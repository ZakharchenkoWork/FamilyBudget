@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import com.faigenbloom.familybudget.common.ui.AnimationState
import com.faigenbloom.familybudget.common.ui.Loading
import com.faigenbloom.familybudget.common.ui.Success
import com.faigenbloom.familybudget.common.ui.dialogs.FailureDialog
import com.faigenbloom.familybudget.common.ui.dialogs.QuestionDialog
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme

@Composable
fun FamilyPage(
    state: FamilyState,
    onBack: () -> Unit,
) {
    Box {
        Column {
            TopBar(
                startIcon = R.drawable.icon_arrow_back,
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
                    FamilyMemberItem(
                        personName = state.familyList[it],
                        index = it,
                        onDeleteFamilyMember = state.onDeleteFamilyMember,
                    )
                }
            }
        }
        Dialogs(state = state)
    }
}

@Composable
private fun Dialogs(
    state: FamilyState,
) {
    if (state.isQRDialogOpened) {
        BasicAlertDialog(onDismissRequest = { state.onQRVisibilityChanged(false) }) {
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

    QuestionDialog(
        isShown = state.migrationDialogState.isMigrationDialogOpened,
        isShowIcon = false,
        title = stringResource(R.string.settings_family_migration_title),
        text = stringResource(R.string.settings_family_new_familyname, state.familyName),
        onOkClick = state.migrationDialogState.onOkClicked,
        onCancelClick = { state.migrationDialogState.onMigrationDialogVisibilityChanged(false) },
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp),
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(0.8f),
                text = stringResource(R.string.settings_family_spendings),
                color = colorScheme.onPrimary,
                style = typography.bodyMedium,
            )
            Switch(
                modifier = Modifier
                    .weight(0.2f),
                checked = state.migrationDialogState.isHideUserSpending,
                onCheckedChange = state.migrationDialogState.onHideUserSpendingChanged,
            )
        }
    }


    Success(state.successState)
    FailureDialog(
        isShown = state.migrationErrorDialogState.isShown,
        text = stringResource(
            if (state.migrationErrorDialogState.isNotFound)
                R.string.settings_family_migration_family_not_found else
                R.string.settings_family_migration_family_same_family,
        ),
        onHideDialog = state.migrationErrorDialogState.onHideDialog,
    )
    QuestionDialog(
        isShown = state.exitFamilyDialogState.isShown,
        isShowIcon = false,
        title = stringResource(R.string.are_you_shure),
        text = stringResource(R.string.leaveFamily),
        onOkClick = state.exitFamilyDialogState.onOkClicked,
        onCancelClick = state.exitFamilyDialogState.onHideDialog,
    ) {
        BaseTextField(
            text = state.exitFamilyDialogState.familyName,
            labelId = R.string.family_name,
            onTextChange = state.exitFamilyDialogState.onDialogFamilyNameChanged,
        )
    }
    Loading(state.isLoading)
}

@Composable
private fun FamilyMemberItem(
    personName: String,
    index: Int,
    onDeleteFamilyMember: (index: Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp,
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = colorScheme.secondary,
                shape = shapes.small,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 16.dp,
                ),
            text = personName,
            color = colorScheme.tertiary,
            style = typography.titleMedium,
        )
        Icon(
            modifier = Modifier
                .size(32.dp)
                .padding(horizontal = 8.dp)
                .aspectRatio(1f)
                .clickable {
                    onDeleteFamilyMember(index)
                },
            painter = painterResource(id = R.drawable.icon_delete),
            contentDescription = "",
            tint = colorScheme.tertiary,
        )
    }
}

@Preview
@Composable
fun FamilyPagePreview() {
    FamillySpandingsTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            FamilyPage(
                state = FamilyState(
                    migrationDialogState = MigrationDialogState(
                        familyName = "Zakharchenko",
                        isMigrationDialogOpened = false,
                        onMigrationDialogVisibilityChanged = {},
                        onHideUserSpendingChanged = {},
                        isHideUserSpending = true,
                        onOkClicked = {},
                    ),
                    successState = AnimationState(
                        isShown = false,
                        onFinish = {},
                    ),
                    familyId = "asdgfasdg",
                    familyName = "Zakharchenko",
                    familyList = listOf(
                        "Konstantyn Zakharchenko",
                        "Victoria Zakharchenko",
                        "Natalia Zakharchenko",
                    ),
                    isQRDialogOpened = false,
                    onQRVisibilityChanged = {},
                    onSave = {},
                    onFamilyNameChanged = {},
                    onDeleteFamilyMember = {},
                    migrationErrorDialogState = MigrationErrorDialogState(
                        isShown = false,
                        isNotFound = false,
                        onHideDialog = {},
                    ),
                    isLoading = false,
                    exitFamilyDialogState = ExitFamilyDialogState(
                        isShown = false,
                        onHideDialog = {},
                        familyName = "",
                        onDialogFamilyNameChanged = {},
                    ),
                    canLeaveFamily = false,
                    onShareLink = {},
                    onLeaveFamily = {},
                ),
                onBack = {},
            )
        }
    }
}

@Preview
@Composable
fun FamilyMigrationPagePreview() {
    FamillySpandingsTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            FamilyPage(
                state = FamilyState(
                    migrationDialogState = MigrationDialogState(
                        familyName = "Zakharchenko",
                        isMigrationDialogOpened = true,
                        onMigrationDialogVisibilityChanged = {},
                        onHideUserSpendingChanged = {},
                        isHideUserSpending = true,
                        onOkClicked = {},
                    ),
                    successState = AnimationState(
                        isShown = false,
                        onFinish = {},
                    ),
                    familyId = "asdgfasdg",
                    familyName = "Zakharchenko",
                    familyList = listOf(
                        "Konstantyn Zakharchenko",
                        "Victoria Zakharchenko",
                        "Natalia Zakharchenko",
                    ),
                    isQRDialogOpened = false,
                    onQRVisibilityChanged = {},
                    onSave = {},
                    onFamilyNameChanged = {},
                    onDeleteFamilyMember = {},
                    migrationErrorDialogState = MigrationErrorDialogState(
                        isShown = false,
                        isNotFound = false,
                        onHideDialog = {},
                    ),
                    canLeaveFamily = false,
                    onShareLink = {},
                    onLeaveFamily = {},
                    exitFamilyDialogState = ExitFamilyDialogState(
                        isShown = false,
                        onHideDialog = {},
                        familyName = "",
                        onDialogFamilyNameChanged = {},
                    ),
                    isLoading = false,
                ),
                onBack = {},
            )
        }
    }
}

@Preview
@Composable
fun FamilyPageQRPreview() {
    FamillySpandingsTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            FamilyPage(
                state = FamilyState(
                    migrationDialogState = MigrationDialogState(
                        familyName = "Zakharchenko",
                        isMigrationDialogOpened = false,
                        onMigrationDialogVisibilityChanged = {},
                        onHideUserSpendingChanged = {},
                        isHideUserSpending = true,
                        onOkClicked = {},
                    ),
                    successState = AnimationState(
                        isShown = false,
                        onFinish = {},
                    ),
                    familyId = "asdgfasdg",
                    familyName = "Zakharchenko",
                    familyList = listOf(
                        "Konstantyn Zakharchenko",
                        "Victoria Zakharchenko",
                        "Natalia Zakharchenko",
                    ),
                    isQRDialogOpened = true,
                    onQRVisibilityChanged = {},
                    onSave = {},
                    onFamilyNameChanged = {},
                    onDeleteFamilyMember = {},
                    migrationErrorDialogState = MigrationErrorDialogState(
                        isShown = false,
                        isNotFound = false,
                        onHideDialog = {},
                    ),
                    canLeaveFamily = false,
                    onShareLink = {},
                    onLeaveFamily = {},
                    exitFamilyDialogState = ExitFamilyDialogState(
                        isShown = false,
                        onHideDialog = {},
                        familyName = "",
                        onDialogFamilyNameChanged = {},
                    ),
                ),
                onBack = {},
            )
        }
    }
}

