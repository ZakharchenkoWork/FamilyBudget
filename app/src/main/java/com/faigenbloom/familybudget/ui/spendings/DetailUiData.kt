package com.faigenbloom.familybudget.ui.spendings

import com.faigenbloom.familybudget.common.Identifiable

data class SpendingDetailListWrapper(
    val details: ArrayList<DetailUiData> = ArrayList(),
)

data class DetailUiData(
    override val id: String,
    val name: String,
    val amount: String,
    val barcode: String = "",
) : Identifiable

