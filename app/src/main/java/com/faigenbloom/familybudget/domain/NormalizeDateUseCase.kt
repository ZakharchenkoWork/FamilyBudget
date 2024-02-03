package com.faigenbloom.familybudget.domain

import android.util.Log
import com.faigenbloom.familybudget.common.getCurrentDate
import com.faigenbloom.familybudget.common.toLongDate
import com.faigenbloom.familybudget.common.toReadableDate
import java.time.format.DateTimeParseException

class NormalizeDateUseCase {
    operator fun invoke(
        date: String,
    ): String {
        return try {
            if (date.isBlank()) {
                getCurrentDate().toReadableDate()
            } else {
                date.toLongDate().toReadableDate()
            }
        } catch (dateTimeParseException: DateTimeParseException) {
            Log.e(javaClass.simpleName, "Handled: $dateTimeParseException")
            invoke("")
        }
    }
}
