@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.comon

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme
import com.faigenbloom.famillyspandings.ui.theme.transparent

@Composable
fun BaseTextField(
    modifier: Modifier = Modifier,
    text: String,
    @StringRes labelId: Int,
    textFieldType: TextFieldType = TextFieldType.Normal,
    onTextChange: (String) -> Unit,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.transparent(),
        ),
        label = {
            Text(
                text = stringResource(id = labelId),
            )
        },
        value = text,
        onValueChange = onTextChange,
        singleLine = true,

        visualTransformation = if (passwordVisible || textFieldType != TextFieldType.Password) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = when (textFieldType) {
            TextFieldType.Password ->
                KeyboardOptions(keyboardType = KeyboardType.Password)

            TextFieldType.Email ->
                KeyboardOptions(keyboardType = KeyboardType.Email)

            TextFieldType.Normal ->
                KeyboardOptions(keyboardType = KeyboardType.Text)

            TextFieldType.Money ->
                KeyboardOptions(keyboardType = KeyboardType.Decimal)
        },

        trailingIcon = {
            if (textFieldType == TextFieldType.Password) {
                val image = if (passwordVisible) {
                    painterResource(id = R.drawable.password_shown)
                } else {
                    painterResource(id = R.drawable.password_hiden)
                }

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, description)
                }
            }
        },
    )
}

enum class TextFieldType {
    Normal,
    Email,
    Money,
    Password,
}

@Preview(showBackground = true)
@Composable
fun LoginTextFieldPreview() {
    FamillySpandingsTheme {
        BaseTextField(
            modifier = Modifier.padding(8.dp),
            labelId = R.string.email,
            text = "loginText",
            textFieldType = TextFieldType.Normal,
            onTextChange = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginTextFieldPreviewEmpty() {
    FamillySpandingsTheme {
        BaseTextField(
            modifier = Modifier.padding(8.dp),
            labelId = R.string.email,
            text = "",
            textFieldType = TextFieldType.Email,
            onTextChange = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginTextFieldPasswordPreview() {
    FamillySpandingsTheme {
        BaseTextField(
            modifier = Modifier.padding(8.dp),
            labelId = R.string.password,
            textFieldType = TextFieldType.Password,
            text = "Password",
            onTextChange = { },
        )
    }
}
