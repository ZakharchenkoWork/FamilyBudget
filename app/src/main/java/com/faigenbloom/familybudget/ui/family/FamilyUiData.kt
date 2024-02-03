package com.faigenbloom.familybudget.ui.family

import com.faigenbloom.familybudget.common.Identifiable

data class FamilyUiData(
    override val id: String,
    val name: String,
    val members: List<PersonUiData>,
) : Identifiable
