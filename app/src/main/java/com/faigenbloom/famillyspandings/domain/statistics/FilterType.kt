package com.faigenbloom.famillyspandings.domain.statistics

import com.faigenbloom.famillyspandings.common.getCurrentDate
import com.faigenbloom.famillyspandings.common.getMonthEndDate
import com.faigenbloom.famillyspandings.common.getMonthStartDate
import com.faigenbloom.famillyspandings.common.getYearEndDate
import com.faigenbloom.famillyspandings.common.getYearStartDate
import com.faigenbloom.famillyspandings.common.toLocalDate
import com.faigenbloom.famillyspandings.common.toLongDate


sealed class FilterType(val from: Long, val to: Long) {
    class Daily(from: Long = getCurrentDate(), to: Long = from) :
        FilterType(from, to) {
        override fun move(isForward: Boolean): FilterType {
            val date = if (isForward) {
                from.toLocalDate().plusDays(1).toLongDate()
            } else {
                from.toLocalDate().minusDays(1).toLongDate()
            }
            return Daily(
                from = date,
                to = date,
            )
        }
    }

    class Monthly(from: Long = getMonthStartDate(), to: Long = getMonthEndDate()) :
        FilterType(from, to) {
        override fun move(isForward: Boolean): FilterType {
            return Monthly(
                from = if (isForward) {
                    from.getMonthStartDate(future = 1)
                } else {
                    from.getMonthStartDate(past = 1)
                },
                to = if (isForward) {
                    to.getMonthEndDate(future = 1)
                } else {
                    to.getMonthEndDate(past = 1)
                },
            )
        }
    }

    class Yearly(from: Long = getYearStartDate(), to: Long = getYearEndDate()) :
        FilterType(from, to) {
        override fun move(isForward: Boolean): FilterType {
            return Yearly(
                from = if (isForward) {
                    from.getYearStartDate(future = 1)
                } else {
                    from.getYearStartDate(past = 1)
                },
                to = if (isForward) {
                    to.getYearEndDate(future = 1)
                } else {
                    to.getYearEndDate(past = 1)
                },
            )
        }
    }

    class Range(from: Long, to: Long) :
        FilterType(from, to) {
        override fun move(isForward: Boolean): FilterType {
            val diff = to - from
            return if (isForward) {
                Range(from = to, to = to + diff)
            } else {
                Range(from = from - diff, to = from)
            }
        }
    }

    abstract fun move(isForward: Boolean): FilterType
}
