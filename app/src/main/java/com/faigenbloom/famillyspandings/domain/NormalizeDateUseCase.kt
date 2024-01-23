package com.faigenbloom.famillyspandings.domain

import android.util.Log
import com.faigenbloom.famillyspandings.common.getCurrentDate
import com.faigenbloom.famillyspandings.common.toLongDate
import com.faigenbloom.famillyspandings.common.toReadableDate
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
