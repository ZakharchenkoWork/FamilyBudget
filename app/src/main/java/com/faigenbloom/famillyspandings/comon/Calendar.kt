@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.comon

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun Calendar() {
    DatePickerDialog(
        modifier = Modifier.fillMaxSize(),
        onDismissRequest = { /*TODO*/ },
        confirmButton = { /*TODO*/ },
    ) {
    }
}
/*
val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.ROOT)
            Text(
                text = "Selected date: ${formatter.format(Date(datePickerState.selectedDateMillis!!))}",
            )
*/

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            Calendar()
        }
    }
}
