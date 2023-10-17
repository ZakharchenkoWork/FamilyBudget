@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.spandings_page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme


@Composable
fun SpandingsPage(state: SpendingsState) {
    LazyVerticalGrid(columns = GridCells.Fixed(3),
        content = {
            items(state.spendings.size) {
                SpendingItem(state.spendings[it])
            }
        })
}

@Composable
fun SpendingItem(item: SpendingData) {
    Card(modifier = Modifier.padding(16.dp)) {
        Box {
            Text(
                text = item.name,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun SpandingsPagePreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            SpandingsPage(state = SpendingsState(spendings = Mock.spendingsList))
        }
    }
}
