@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.common.BaseButton
import com.faigenbloom.famillyspandings.common.BaseTextField
import com.faigenbloom.famillyspandings.common.StripeBar
import com.faigenbloom.famillyspandings.common.TextFieldType
import com.faigenbloom.famillyspandings.common.TopBar
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun LoginPage(
    state: LoginPageState,
    onBack: () -> Unit,
) {
    Column {
        TopBar(
            startIcon = R.drawable.icon_arrow_back,
            onStartIconCLicked = onBack,
        )
        StripeBar(textId = R.string.authorization)
        BaseTextField(
            modifier = Modifier.padding(top = 100.dp),
            labelId = R.string.email,
            text = state.loginText,
            isError = state.authError,
            textFieldType = TextFieldType.Email,
            onTextChange = state.onLoginChanged,
        )
        BaseTextField(
            labelId = R.string.password,
            text = state.passwordText,
            isError = state.authError,
            textFieldType = TextFieldType.Password,
            onTextChange = state.onPasswordChanged,
        )
        ForgotPassword(onClick = state.onForgotPasswordClicked)
        if (state.authError) {
            AuthError()
        }
        BaseButton(
            modifier = Modifier.padding(top = 16.dp),
            textRes = R.string.login,
            onClick = state.onLoginClicked,
        )
    }
}

@Composable
fun AuthError() {
    Text(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        text = stringResource(id = R.string.wrong_password),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error,
    )
}

@Composable
fun ForgotPassword(
    onClick: () -> Unit,
) {
    Text(
        modifier = Modifier
            .padding(16.dp)
            .clickable { onClick() },
        text = stringResource(id = R.string.forget_password),
        style = TextStyle(textDecoration = TextDecoration.Underline),
        color = MaterialTheme.colorScheme.secondary,
    )
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            LoginPage(
                state = LoginPageState(
                    loginText = Mock.loginText,
                    passwordText = Mock.passwordText,
                    authError = true,
                    onLoginClicked = {},
                    onLoginChanged = {},
                    onPasswordChanged = {},
                    onForgotPasswordClicked = {},
                ),
                onBack = {},
            )
        }
    }
}
