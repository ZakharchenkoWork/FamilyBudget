package com.faigenbloom.familybudget.ui.settings

import java.util.Currency

data class SettingUiData(
    val currency: Currency,
    val isNotificationsEnabled: Boolean,
    val isPasswordEnabled: Boolean,
    val name: String,
    val familyName: String,
)
