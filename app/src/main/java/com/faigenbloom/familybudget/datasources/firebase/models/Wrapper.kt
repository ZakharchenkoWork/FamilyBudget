package com.faigenbloom.familybudget.datasources.firebase.models

import kotlinx.serialization.Serializable

@Serializable
data class Wrapper<T>(val list: List<T>)
