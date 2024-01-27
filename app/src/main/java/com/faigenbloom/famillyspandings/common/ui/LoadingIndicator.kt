package com.faigenbloom.famillyspandings.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Preview
@Composable
fun LoadingIndicatorPreview() {
    FamillySpandingsTheme {
        Surface {
            LoadingIndicator()
        }
    }
}
