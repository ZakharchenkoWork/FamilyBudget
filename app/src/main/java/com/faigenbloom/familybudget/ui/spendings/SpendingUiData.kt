package com.faigenbloom.familybudget.ui.spendings

import android.net.Uri
import com.faigenbloom.familybudget.common.Identifiable

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
    val isDuplicate: Boolean = false,
) : Identifiable
