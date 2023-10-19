@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
fun BottomNavigationBar(onDestinationChanged: (Destination) -> Unit) {
    val items = listOf(
        BarItem(
            destination = Destination.SpendingsPage,
            imageChecked = R.drawable.list_filled,
            imageUnChecked = R.drawable.list_outlined,
        ),
        BarItem(
            destination = Destination.SpendingsPage,
            imageChecked = R.drawable.list_filled,
            imageUnChecked = R.drawable.list_outlined,
        ),
        BarItem(
            destination = Destination.SpendingsPage,
            imageChecked = R.drawable.list_filled,
            imageUnChecked = R.drawable.list_outlined,
        ),
        BarItem(
            destination = Destination.SpendingsPage,
            imageChecked = R.drawable.list_filled,
            imageUnChecked = R.drawable.list_outlined,
        ),
    )
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
    NavigationBar(
        modifier = Modifier.fillMaxHeight(0.05f),
    ) {
        items.forEachIndexed { index, barItem ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    onDestinationChanged(barItem.destination)
                },
                icon = {
                    Image(
                        modifier = Modifier.padding(16.dp),
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
        BottomNavigationBar(onDestinationChanged = {})
    }
}
