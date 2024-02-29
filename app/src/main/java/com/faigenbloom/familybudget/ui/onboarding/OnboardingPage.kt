package com.faigenbloom.familybudget.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.BaseButton
import com.faigenbloom.familybudget.common.ui.Loading
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme
import com.faigenbloom.familybudget.ui.theme.circle

@Composable
fun OnboardingPage(
    state: OnboardingState,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onQrScan: () -> Unit,
) {
    val isLoading by remember { state.isLoading }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.secondary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (isLoading.not()) {
            Arrangement.Center
        } else {
            Arrangement.Top
        },
    ) {
        Box(
            modifier = Modifier
                .padding(top = 24.dp)
                .clip(MaterialTheme.shapes.circle())
                .background(color = MaterialTheme.colorScheme.tertiary),
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
            color = MaterialTheme.colorScheme.primary,
        )
        if (isLoading.not()) {
            BaseButton(
                modifier = Modifier.padding(top = 24.dp),
                textRes = R.string.login,
                onClick = onLogin,
            )
            BaseButton(
                modifier = Modifier.padding(top = 24.dp),
                textRes = R.string.registration,
                onClick = onRegister,
            )
            BaseButton(
                modifier = Modifier.padding(top = 24.dp),
                textRes = R.string.join,
                onClick = onQrScan,
            )
        }
    }
    Loading(isShown = isLoading)
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun OnboardingPagePreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            OnboardingPage(
                state = OnboardingState(),
                onLogin = {},
                onRegister = {},
                onQrScan = {},
            )
        }
    }
}
