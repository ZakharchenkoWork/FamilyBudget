package com.faigenbloom.famillyspandings.ui.family

import com.faigenbloom.famillyspandings.common.Identifiable

data class PersonUiData(
    override val id: String,
    val familyId: String,
    val familyName: String,
    val name: String,
    val isThisUser: Boolean,
) : Identifiable
