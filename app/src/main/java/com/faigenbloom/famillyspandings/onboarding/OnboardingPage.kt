@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.onboarding

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme
import com.faigenbloom.famillyspandings.ui.theme.circle

@Composable
fun OnboardingPage(
    onLogin: () -> Unit,
    onRegister: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.secondary),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .padding(top = 24.dp)
                .clip(MaterialTheme.shapes.circle())
                .background(color = MaterialTheme.colorScheme.tertiary)
        ) {
            Image(
                modifier = Modifier
                    .padding(32.dp),
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "",
            )
        }

        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        OnboardingButton(
            textRes = R.string.login,
            onClick = onLogin
        )
        OnboardingButton(
            textRes = R.string.registration,
            onClick = onRegister
        )
    }
}

@Composable
fun OnboardingButton(@StringRes textRes: Int, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(top = 24.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary),
        onClick = onClick,
    ) {
        Text(
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun OnboardingPagePreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            OnboardingPage(
                onLogin = {},
                onRegister = {}
            )
        }
    }
}
