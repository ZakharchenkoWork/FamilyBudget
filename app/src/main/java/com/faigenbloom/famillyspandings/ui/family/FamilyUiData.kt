package com.faigenbloom.famillyspandings.ui.family

import com.faigenbloom.famillyspandings.common.Identifiable

data class FamilyUiData(
    override val id: String,
    val name: String,
    val members: List<PersonUiData>,
) : Identifiable
