@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun Categories(state: CategoriesState) {
    LazyColumn(
        content = {
            items(state.categoriesList.size) { itemIndex ->
                val item = state.categoriesList[itemIndex]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(
                            color = if (state.selectedIndex == itemIndex) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.tertiary
                            },
                        )
                        .clickable {
                            state.onSelectionChanged(itemIndex)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    item.iconId?.let {
                        Image(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(64.dp),
                            painter = painterResource(id = it),
                            contentDescription = null,
                        )
                    }
                    item.nameId?.let {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .weight(1f),
                            text = stringResource(id = it),
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    if (state.selectedIndex == itemIndex) {
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            painter = painterResource(id = R.drawable.ok),
                            contentDescription = null,
                        )
                    }
                }
            }
        },
    )
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun SpendingEditPagePreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            Categories(
                state = CategoriesState(
                    categoriesList = Mock.categoriesList,
                    selectedIndex = 1,
                    onSelectionChanged = {},
                ),
            )
        }
    }
}
