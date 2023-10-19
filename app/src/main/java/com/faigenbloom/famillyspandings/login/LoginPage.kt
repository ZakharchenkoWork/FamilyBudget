@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.login

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.comon.BaseButton
import com.faigenbloom.famillyspandings.comon.BaseTextField
import com.faigenbloom.famillyspandings.comon.TextFieldType
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun LoginPage(state: LoginPageState) {
    Column {
        TopBar()
        StripeTitle(textId = R.string.authorization)
        BaseTextField(
            modifier = Modifier.padding(top = 100.dp),
            labelId = R.string.email,
            text = state.loginText,
            textFieldType = TextFieldType.Email,
            onTextChange = state.onLoginChanged,
        )
        BaseTextField(
            labelId = R.string.password,
            text = state.passwordText,
            textFieldType = TextFieldType.Password,
            onTextChange = state.onPasswordChanged,
        )
        ForgotPassword(onClick = state.onForgotPasswordClicked)
        BaseButton(
            modifier = Modifier.padding(top = 16.dp),
            textRes = R.string.login,
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
                    onLoginClicked = {},
                    onLoginChanged = {},
                    onPasswordChanged = {},
                    onForgotPasswordClicked = {},
                ),
            )
        }
    }
}
