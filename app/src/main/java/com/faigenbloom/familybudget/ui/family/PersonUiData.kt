package com.faigenbloom.familybudget.ui.family

import com.faigenbloom.familybudget.common.Identifiable

data class PersonUiData(
    override val id: String,
    val familyId: String,
    val familyName: String,
    val name: String,
    val isThisUser: Boolean,
) : Identifiable
