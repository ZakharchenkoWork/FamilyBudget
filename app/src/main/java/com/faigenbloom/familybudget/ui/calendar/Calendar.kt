@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
)

package com.faigenbloom.familybudget.ui.calendar

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
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.toLongDate
import com.faigenbloom.familybudget.common.toReadableDate
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme

@Composable
fun Calendar(
    startDate: String,
    endDate: String = "",
    onDatePicked: (String, String) -> Unit,
) {
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = startDate.toLongDate(),
        initialSelectedEndDateMillis = endDate.toLongDate(),
    )

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis =
        startDate.toLongDate(),
    )

    Box {
        if (endDate.isNotBlank()) {
            DateRangePickerComposable(state = dateRangePickerState)
        } else {
            DatePickerComposable(state = datePickerState)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        if (endDate.isBlank()) {
                            datePickerState.selectedDateMillis?.let {
                                onDatePicked(it.toReadableDate(), "")
                            }
                        } else {
                            dateRangePickerState.selectedStartDateMillis?.let { start ->
                                dateRangePickerState.selectedEndDateMillis?.let { end ->
                                    onDatePicked(start.toReadableDate(), end.toReadableDate())
                                } ?: onDatePicked(start.toReadableDate(), start.toReadableDate())
                            }
                        }
                    },
                painter = painterResource(id = R.drawable.icon_ok),
                contentDescription = null,
            )
        }
    }
}


@Composable

fun DatePickerComposable(
    state: DatePickerState,
) {
    DatePicker(
        modifier = Modifier
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(5),
            ),
        state = state,
        colors = getDatePickerColors(),
    )
}

@Composable
fun DateRangePickerComposable(
    state: DateRangePickerState,
) {
    DateRangePicker(
        modifier = Modifier
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(5),
            ),
        state = state,
        /*  dateFormatter =  DatePickerFormatter(
              selectedDateSkeleton = "dd.MM.YYYY",
          ),*/
        colors = getDatePickerColors(),
    )
}

@Composable
fun getDatePickerColors() = DatePickerDefaults.colors(
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
    dayInSelectionRangeContentColor = MaterialTheme.colorScheme.primary,
    dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.onBackground,
    todayContentColor = MaterialTheme.colorScheme.onBackground,
    todayDateBorderColor = MaterialTheme.colorScheme.onBackground,
)


@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    FamillySpandingsTheme {
        Surface {
            Calendar(
                startDate = "",
                onDatePicked = { _, _ -> },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarPickerPreview() {
    FamillySpandingsTheme {
        Surface {
            Calendar(
                startDate = "20.01.2024",
                endDate = "24.01.2024",
                onDatePicked = { _, _ -> },
            )
        }
    }
}
