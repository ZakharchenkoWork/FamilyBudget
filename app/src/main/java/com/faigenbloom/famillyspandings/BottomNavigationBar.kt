@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.comon.Destination
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

data class BarItem(
    val destination: Destination,
    val imageChecked: Int,
    val imageUnChecked: Int,
)

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onDestinationChanged: (Int, Destination) -> Unit,
) {
    val items = listOf(
        BarItem(
            destination = Destination.SpendingsPage,
            imageChecked = R.drawable.icon_list_filled,
            imageUnChecked = R.drawable.icon_list_outlined,
        ),
        BarItem(
            destination = Destination.BudgetPage,
            imageChecked = R.drawable.icon_budget_filled,
            imageUnChecked = R.drawable.icon_budget_outlined,
        ),
        BarItem(
            destination = Destination.NewSpendingPage,
            imageChecked = R.drawable.icon_plus_filled,
            imageUnChecked = R.drawable.icon_plus_outlined,
        ),
        BarItem(
            destination = Destination.StatisticsPage,
            imageChecked = R.drawable.icon_statistics_filled,
            imageUnChecked = R.drawable.icon_statistics_outlined,
        ),
        BarItem(
            destination = Destination.SettingsPage,
            imageChecked = R.drawable.icon_settings_filled,
            imageUnChecked = R.drawable.icon_settings_outlined,
        ),
    )

    NavigationBar(
        modifier = Modifier.height(40.dp),
    ) {
        items.forEachIndexed { index, barItem ->
            NavigationBarItem(
                modifier = Modifier
                    .padding(4.dp)
                    .semantics { contentDescription = barItem.destination.route },
                selected = selectedIndex == index,
                onClick = {
                    onDestinationChanged(index, barItem.destination)
                },
                icon = {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        painter = painterResource(
                            id = if (selectedIndex == index) {
                                barItem.imageChecked
                            } else {
                                barItem.imageUnChecked
                            },
                        ),
                        contentDescription = null,
                    )
                },
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun HomePagePreview() {
    FamillySpandingsTheme {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    selectedIndex = 0,
                    onDestinationChanged = { _, _ -> },
                )
            },
        ) {
        }
    }
}
