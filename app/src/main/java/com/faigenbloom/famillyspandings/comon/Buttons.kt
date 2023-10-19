package com.faigenbloom.famillyspandings.comon

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary),
        shape = RectangleShape,
        contentPadding = PaddingValues(0.dp),
        enabled = isEnabled,
        onClick = onClick,
    ) {
        Text(
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPagePreview() {
    FamillySpandingsTheme {
        BaseButton(
            onClick = {},
            textRes = R.string.app_name,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPageDisabledPreview() {
    FamillySpandingsTheme {
        BaseButton(
            onClick = {},
            textRes = R.string.app_name,
            isEnabled = false,
        )
    }
}
