@file:OptIn(
    ExperimentalMaterial3Api::class,
)

package com.faigenbloom.familybudget.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.ui.MoneyTextTransformation
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme
import com.faigenbloom.familybudget.ui.theme.hint
import com.faigenbloom.familybudget.ui.theme.transparent
import java.util.Currency

@Composable
fun BaseTextField(
    modifier: Modifier = Modifier,
    text: String,
    @StringRes labelId: Int,
    @StringRes errorTextId: Int = R.string.empty,
    isError: Boolean = false,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    textFieldType: TextFieldType = TextFieldType.Normal,
    onTextChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val containerColor = MaterialTheme.colorScheme.transparent()
    TextField(
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
        ),
        label = {
            Text(
                text = stringResource(id = labelId),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        },
        isError = isError,
        value = text,
        onValueChange = onTextChange,
        singleLine = true,
        keyboardActions = keyboardActions,
        visualTransformation = if (passwordVisible || textFieldType != TextFieldType.Password) {
            if (textFieldType is TextFieldType.Money) {
                MoneyTextTransformation(textFieldType.currency.currencyCode)
            } else {
                VisualTransformation.None
            }
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = textFieldType.keyboardOptions,
        supportingText = {
            if (isError) {
                Text(text = stringResource(id = errorTextId))
            }
        },
        trailingIcon = if (textFieldType == TextFieldType.Password) {
            {
                val image = if (passwordVisible) {
                    painterResource(id = R.drawable.icon_shown)
                } else {
                    painterResource(id = R.drawable.icon_hidden)
                }

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, description)
                }
            }
        } else {
            null
        },
    )
}

@Composable
fun SimpleTextField(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    textStyle: TextStyle = TextStyle.Default,
    textAlign: TextAlign = TextAlign.Start,
    textFieldType: TextFieldType = TextFieldType.Normal,
    onValueChange: (String) -> Unit,
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.secondary,
        backgroundColor = MaterialTheme.colorScheme.transparent(),
    )
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            modifier = modifier,
            value = text,
            textStyle = textStyle.copy(textAlign = textAlign),
            keyboardOptions = textFieldType.keyboardOptions,
            onValueChange = onValueChange,
            singleLine = true,
            visualTransformation = if (textFieldType is TextFieldType.Money) {
                MoneyTextTransformation(textFieldType.currency.currencyCode)
            } else {
                VisualTransformation.None
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
            decorationBox = { innerTextField ->
                if (text.isBlank()) {
                    Text(
                        modifier = modifier,
                        text = label,
                        textAlign = textAlign,
                        color = MaterialTheme.colorScheme.hint(),
                        style = MaterialTheme.typography.bodySmall,
                    )
                } else {
                    innerTextField()
                }
            },
        )
    }
}

sealed class TextFieldType(val keyboardOptions: KeyboardOptions) {
    data object Normal :
        TextFieldType(KeyboardOptions(keyboardType = KeyboardType.Text))

    data object Email :
        TextFieldType(KeyboardOptions(keyboardType = KeyboardType.Email))

    data class Money(val currency: Currency) :
        TextFieldType(KeyboardOptions(keyboardType = KeyboardType.Decimal))

    data object Password :
        TextFieldType(KeyboardOptions(keyboardType = KeyboardType.Email))
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

@Preview(showBackground = true)
@Composable
fun TextFieldPreviewError() {
    FamillySpandingsTheme {
        BaseTextField(
            modifier = Modifier.padding(8.dp),
            labelId = R.string.password,
            textFieldType = TextFieldType.Normal,
            text = "Name",
            isError = true,
            onTextChange = { },
        )
    }
}
