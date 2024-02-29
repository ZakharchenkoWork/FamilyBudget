package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.common.getMonthEndDate
import com.faigenbloom.familybudget.common.getMonthStartDate
import com.faigenbloom.familybudget.common.getYearEndDate
import com.faigenbloom.familybudget.common.getYearStartDate

sealed class FilterType(val from: Long, val to: Long, val isPlanned: Boolean) {
    var isMovedForward: Boolean? = null

    class Daily(
        from: Long = getMonthStartDate(),
        to: Long = getMonthEndDate(),
        isPlanned: Boolean = false,
    ) : FilterType(from, to, isPlanned) {
        constructor(isPlanned: Boolean) : this(
            from = getMonthStartDate(),
            to = getMonthEndDate(future = if (isPlanned) 1 else 0),
            isPlanned = isPlanned,
        )

        override fun move(isForward: Boolean): FilterType {
            return Daily(
                from = if (isForward) {
                    from.getMonthStartDate(future = if (isPlanned.not()) 1 else 2)
                } else {
                    from.getMonthStartDate(past = if (isPlanned.not()) 1 else 2)
                },
                to = if (isForward) {
                    to.getMonthEndDate(future = if (isPlanned.not()) 1 else 2)
                } else {
                    to.getMonthEndDate(past = if (isPlanned.not()) 1 else 2)
                },
                isPlanned = isPlanned,
            ).also {
                it.isMovedForward = isForward
            }
        }
    }

    class Monthly(
        from: Long = getYearStartDate(), to: Long = getYearEndDate(),
        isPlanned: Boolean = false,
    ) : FilterType(from, to, isPlanned) {
        constructor(isPlanned: Boolean) : this(
            from = getYearStartDate(),
            to = getYearEndDate(),
            isPlanned = isPlanned,
        )

        override fun move(isForward: Boolean): FilterType {
            return Monthly(
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
                isPlanned = isPlanned,
            ).also {
                it.isMovedForward = isForward
            }
        }
    }

    class Yearly(
        from: Long = getYearStartDate(past = 5),
        to: Long = getYearEndDate(),
        isPlanned: Boolean = false,
    ) : FilterType(from, to, isPlanned) {
        constructor(isPlanned: Boolean) : this(
            from = getYearStartDate(past = 5),
            to = getYearEndDate(future = 5),
            isPlanned = isPlanned,
        )

        override fun move(isForward: Boolean): FilterType {
            return Yearly(
                from = if (isForward) {
                    from.getYearStartDate(future = 5)
                } else {
                    from.getYearStartDate(past = 5)
                },
                to = if (isForward) {
                    to.getYearEndDate(future = 5)
                } else {
                    to.getYearEndDate(past = 5)
                },
                isPlanned = isPlanned,
            ).also {
                it.isMovedForward = isForward
            }
        }
    }

    abstract fun move(isForward: Boolean): FilterType
    fun copy(
        from: Long = this.from,
        to: Long = this.to,
        isPlanned: Boolean = this.isPlanned,
    ): FilterType {
        return when (this) {
            is Daily -> Daily(from, to, isPlanned)
            is Monthly -> Monthly(from, to, isPlanned)
            is Yearly -> Yearly(from, to, isPlanned)
        }
    }
}
