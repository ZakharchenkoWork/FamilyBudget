package com.faigenbloom.famillyspandings.domain.spendings

import com.faigenbloom.famillyspandings.common.getMonthEndDate
import com.faigenbloom.famillyspandings.common.getMonthStartDate
import com.faigenbloom.famillyspandings.common.getYearEndDate
import com.faigenbloom.famillyspandings.common.getYearStartDate

sealed class FilterType(val from: Long, val to: Long) {
    class Daily(from: Long = getMonthStartDate(), to: Long = getMonthEndDate()) :
        FilterType(from, to) {
        constructor(isPlanned: Boolean) : this(
            from = getMonthStartDate(),
            to = getMonthEndDate(future = if (isPlanned) 1 else 0),
        )

    }

    class Monthly(from: Long = getYearStartDate(), to: Long = getYearEndDate()) :
        FilterType(from, to) {
        constructor(isPlanned: Boolean) : this(
            from = getYearStartDate(),
            to = getYearEndDate(),
        )
    }

    class Yearly(from: Long = getYearStartDate(past = 5), to: Long = getYearEndDate()) :
        FilterType(from, to) {

        constructor(isPlanned: Boolean) : this(
            from = getYearStartDate(past = 5),
            to = getYearEndDate(future = 5),
        )
    }
}
