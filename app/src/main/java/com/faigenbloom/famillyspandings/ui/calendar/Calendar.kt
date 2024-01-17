@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings.ui.calendar

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.comon.toLongDate
import com.faigenbloom.famillyspandings.comon.toReadableDate
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun Calendar(
    startDate: String,
    onDatePicked: (String) -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis =
        startDate.toLongDate(),
    )
    Box {
        DatePicker(
            modifier = Modifier
                .padding(4.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(5),
                ),
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                weekdayContentColor = MaterialTheme.colorScheme.onBackground,
                subheadContentColor = MaterialTheme.colorScheme.onBackground,
                yearContentColor = MaterialTheme.colorScheme.onBackground,
                currentYearContentColor = MaterialTheme.colorScheme.onBackground,
                selectedYearContainerColor = MaterialTheme.colorScheme.onBackground,
                selectedYearContentColor = MaterialTheme.colorScheme.onBackground,
                dayContentColor = MaterialTheme.colorScheme.onBackground,
                headlineContentColor = MaterialTheme.colorScheme.onBackground,
                selectedDayContentColor = MaterialTheme.colorScheme.primary,
                selectedDayContainerColor = MaterialTheme.colorScheme.onBackground,
                dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onBackground,
                dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.onBackground,
                todayContentColor = MaterialTheme.colorScheme.onBackground,
                todayDateBorderColor = MaterialTheme.colorScheme.onBackground,
            ),
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        datePickerState.selectedDateMillis?.let {
                            onDatePicked(it.toReadableDate())
                        }
                    },
                painter = painterResource(id = R.drawable.icon_ok),
                contentDescription = null,
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    FamillySpandingsTheme {
        Scaffold { _ ->
            Calendar(
                startDate = "",
                onDatePicked = {},
            )
        }
    }
}
