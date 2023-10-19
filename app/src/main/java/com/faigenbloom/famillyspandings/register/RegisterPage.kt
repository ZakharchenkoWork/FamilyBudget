@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.register

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.comon.BaseButton
import com.faigenbloom.famillyspandings.comon.BaseTextField
import com.faigenbloom.famillyspandings.comon.TextFieldType
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun RegisterPage(state: RegisterPageState) {
    Column {
        TopBar()
        StripeTitle(textId = R.string.registration)
        BaseTextField(
            modifier = Modifier.padding(top = 100.dp),
            labelId = R.string.family_name,
            text = state.familyNameText,
            errorTextId = R.string.family_name_error,
            isError = state.familyNameError,
            textFieldType = TextFieldType.Normal,
            onTextChange = state.onFamilyNameChanged,
        )
        BaseTextField(
            labelId = R.string.name,
            text = state.nameText,
            errorTextId = R.string.name_error,
            isError = state.nameError,
            textFieldType = TextFieldType.Normal,
            onTextChange = state.onNameChanged,
        )
        BaseTextField(
            labelId = R.string.email,
            text = state.emailText,
            errorTextId = R.string.email_error,
            isError = state.emailError,
            textFieldType = TextFieldType.Email,
            onTextChange = state.onEmailChanged,
        )
        BaseTextField(
            labelId = R.string.password,
            text = state.passwordText,
            errorTextId = R.string.password_error,
            isError = state.passwordError,
            textFieldType = TextFieldType.Password,
            onTextChange = state.onPasswordChanged,
        )
        PrivacyPolicy(onClick = state.onPrivacyPolicyClicked)
        BaseButton(
            modifier = Modifier.padding(top = 16.dp),
            textRes = R.string.registration,
            isEnabled = state.isRegistrationEnabled,
            onClick = state.onLoginClicked,
        )
    }
}

@Composable
fun TopBar() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
fun StripeTitle(@StringRes textId: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondary),
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = stringResource(id = textId),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Composable
fun PrivacyPolicy(
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
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium,
        onClick = { onClick() },
    )
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun RegisterPagePreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
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
                    onLoginClicked = {},
                    onEmailChanged = {},
                    onPasswordChanged = {},
                    onPrivacyPolicyClicked = {},
                    onNameChanged = {},
                    onFamilyNameChanged = {},
                ),
            )
        }
    }
}
