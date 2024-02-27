@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.familybudget.ui.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.BaseButton
import com.faigenbloom.familybudget.common.BaseTextField
import com.faigenbloom.familybudget.common.StripeBar
import com.faigenbloom.familybudget.common.TextFieldType
import com.faigenbloom.familybudget.common.TopBar
import com.faigenbloom.familybudget.common.ui.dialogs.FailureDialog
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme

@Composable
fun RegisterPage(
    state: RegisterPageState,
    onBack: () -> Unit,
) {
    Column {
        TopBar(
            startIcon = R.drawable.icon_arrow_back,
            onStartIconCLicked = onBack,
        )
        StripeBar(textId = R.string.registration)
        Spacer(modifier = Modifier.size(80.dp))
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            FamilyNameFields(state = state)
            CommonFields(state = state)
            PrivacyPolicy(onClick = state.onPrivacyPolicyClicked)
            BaseButton(
                modifier = Modifier.padding(top = 16.dp),
                textRes = R.string.registration,
                isEnabled = state.isRegistrationEnabled,
                onClick = state.onRegisterClicked,
            )
        }
    }
    FailureDialog(
        isShown = state.migrationErrorDialogState.isShown,
        text = stringResource(R.string.settings_family_migration_family_not_found),
        onHideDialog = state.migrationErrorDialogState.onHideDialog,
    )
}

@Composable
private fun FamilyNameFields(state: RegisterPageState) {
    if (state.isForFamily) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.joining_family, state.familyNameText),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
        )
    } else {
        BaseTextField(
            modifier = Modifier
                .fillMaxWidth(),
            labelId = R.string.family_name,
            text = state.familyNameText,
            errorTextId = R.string.family_name_error,
            isError = state.familyNameError,
            textFieldType = TextFieldType.Normal,
            onTextChange = state.onFamilyNameChanged,
        )
    }
    BaseTextField(
        modifier = Modifier.fillMaxWidth(),
        labelId = R.string.surname,
        text = state.surNameText,
        textFieldType = TextFieldType.Normal,
        onTextChange = state.onSurNameChanged,
    )
}

@Composable
private fun CommonFields(state: RegisterPageState) {
    BaseTextField(
        modifier = Modifier
            .fillMaxWidth(),
        labelId = R.string.name,
        text = state.nameText,
        errorTextId = R.string.name_error,
        isError = state.nameError,
        textFieldType = TextFieldType.Normal,
        onTextChange = state.onNameChanged,
    )
    BaseTextField(
        modifier = Modifier
            .fillMaxWidth(),
        labelId = R.string.email,
        text = state.emailText,
        errorTextId = R.string.email_error,
        isError = state.emailError,
        textFieldType = TextFieldType.Email,
        onTextChange = state.onEmailChanged,
    )
    BaseTextField(
        modifier = Modifier
            .fillMaxWidth(),
        labelId = R.string.password,
        text = state.passwordText,
        errorTextId = R.string.password_error,
        isError = state.passwordError,
        textFieldType = TextFieldType.Password,
        onTextChange = state.onPasswordChanged,
    )
}

@Composable
private fun PrivacyPolicy(
    onClick: () -> Unit,
) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onPrimary)) {
            append("${stringResource(id = R.string.privacy_policy_start)} ")
        }

        pushStringAnnotation(tag = "", annotation = "")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
            append(stringResource(id = R.string.privacy_policy_end))
        }
        pop()
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium,
        onClick = { onClick() },
    )
}

@Preview(showBackground = true)
@Composable
private fun RegisterPagePreview() {
    FamillySpandingsTheme {
        Surface {
            RegisterPage(
                state = RegisterPageState(
                    emailText = Mock.loginText,
                    nameText = Mock.nameText,
                    familyNameText = Mock.familyNameText,
                    passwordText = Mock.passwordText,
                    nameError = false,
                    familyNameError = false,
                    registerError = false,
                    passwordError = false,
                    emailError = false,
                    isRegistrationEnabled = false,
                    isSameFamilyName = true,
                    onEmailChanged = {},
                    onPasswordChanged = {},
                    onNameChanged = {},
                    onFamilyNameChanged = {},
                    onSameFamilyNameSwitched = {},
                    onPrivacyPolicyClicked = {},
                    onRegisterClicked = {},
                    isForFamily = false,
                    surNameText = "",
                    onSurNameChanged = {},
                ),
                onBack = {},
            )
        }
    }
}
