package com.faigenbloom.famillyspandings.domain.spendings

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faigenbloom.famillyspandings.domain.categories.GetCategoryByIdUseCase
import com.faigenbloom.famillyspandings.domain.spendings.dividers.DayGroupDivider
import com.faigenbloom.famillyspandings.domain.spendings.dividers.MonthGroupDivider
import com.faigenbloom.famillyspandings.domain.spendings.dividers.YearGroupDivider
import com.faigenbloom.famillyspandings.repositories.SpendingsRepository
import com.faigenbloom.famillyspandings.ui.categories.CategoryUiData
import com.faigenbloom.famillyspandings.ui.spendings.list.SpendingCategoryUiData
import com.faigenbloom.famillyspandings.ui.spendings.mappers.SpendingsWithCategoryMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class SpendingsPagingSource(
    private val spendingsRepository: SpendingsRepository,
    private val sortPlatesUseCase: SortPlatesUseCase<SpendingCategoryUiData>,
    private val spendingsWithCategoryMapper: SpendingsWithCategoryMapper,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase<CategoryUiData>,

    ) : PagingSource<FilterType, DatedList>() {
    var lowestFrom = 0L
    var highestTo = 0L

    override suspend fun load(params: LoadParams<FilterType>): LoadResult<FilterType, DatedList> {
        return try {
            withContext(Dispatchers.IO) {
                var filter = params.key ?: FilterType.Daily()

                if (highestTo == 0L && lowestFrom == 0L) {
                    val datesRange = spendingsRepository.getSpendingsMinMaxDate(filter.isPlanned)
                    lowestFrom = datesRange.min
                    highestTo = datesRange.max
                }

                var spendings: List<SpendingCategoryUiData>
                do {
                    spendings = spendingsRepository.getSpendingsByDate(
                        isPlanned = filter.isPlanned,
                        from = filter.from,
                        to = filter.to,
                    ).map {
                        spendingsWithCategoryMapper.forUI(it, getCategoryByIdUseCase(it.categoryId))
                    }
                    val isMovedForward = filter.isMovedForward
                    if (spendings.isEmpty() && isMovedForward != null) {

                        if (isMovedForward && highestTo < filter.to) {
                            break
                        }
                        if (!isMovedForward && lowestFrom > filter.from) {
                            break
                        }
                        filter = filter.move(isMovedForward)
                    } else {

                    }
                } while (spendings.isEmpty())


                val data = sortPlatesUseCase(
                    getDividerForFilter(filter),
                    spendings,
                ).map {
                    DatedList(it)
                }
                LoadResult.Page(
                    data = data,
                    prevKey = filter.move(true).let { if (highestTo > filter.to) it else null },
                    nextKey = filter.move(false).let { if (lowestFrom < filter.from) it else null },
                )
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<FilterType, DatedList>): FilterType? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.move(true) ?: page.prevKey?.move(false)
    }
}

fun getDividerForFilter(filterType: FilterType): Divider<SpendingCategoryUiData> {
    return when (filterType) {
        is FilterType.Daily -> DayGroupDivider(filterType.from, filterType.to)
        is FilterType.Monthly -> MonthGroupDivider(filterType.from, filterType.to)
        is FilterType.Yearly -> YearGroupDivider(filterType.from, filterType.to)
    }
}
