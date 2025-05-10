package com.faigenbloom.familybudget.ui.spendings

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.faigenbloom.familybudget.common.Identifiable
import com.faigenbloom.familybudget.R

data class SpendingUiData(
    override val id: String,
    val name: String,
    val amount: String,
    val date: String,
    val categoryId: String,
    val photoUri: Uri?,
    val isPlanned: Boolean,
    val isHidden: Boolean,
    val isManualTotal: Boolean,
    val ownerId: String,
    val isDuplicate: Boolean = false,
    val repeatOptions: RepeatableOptionDataUi?,
) : Identifiable

enum class RepeatOptionsUi(@DrawableRes val icon: Int, @StringRes val stringResource: Int) {
    NONE(R.drawable.icon_repeat, R.string.spendings_filter_never),
    DAILY(R.drawable.icon_repeat_daily, R.string.spendings_filter_daily ),
    WEEKLY(R.drawable.icon_repeat_weekly, R.string.spendings_filter_weekly),
    MONTHLY(R.drawable.icon_repeat_monthly, R.string.spendings_filter_monthly),
    YEARLY(R.drawable.icon_repeat_yearly, R.string.spendings_filter_yearly)
}
data class RepeatableOptionDataUi(
    val id: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val repeatType: RepeatOptionsUi,
    val excludedIDs: List<String> = emptyList()
)
