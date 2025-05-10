package com.faigenbloom.familybudget.datasources.firebase.models

import kotlinx.serialization.Serializable

@Serializable
data class RepeatableOptionModel(
    val id: String,
    val startDate: Long,
    val endDate: Long,
    val repeatType: Int,
    val excludedIDs: List<String>
)
