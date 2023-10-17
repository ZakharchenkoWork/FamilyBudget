package com.faigenbloom.famillyspandings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.faigenbloom.famillyspandings.spandings_page.SpandingsPage
import com.faigenbloom.famillyspandings.spandings_page.SpendingsPageViewModel
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FamillySpandingsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = koinViewModel<SpendingsPageViewModel>()
                    val state by viewModel.spendingsStateFlow.collectAsState()

                    SpandingsPage(state)
                }
            }
        }
    }
}

