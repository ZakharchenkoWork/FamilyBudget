package com.faigenbloom.familybudget.common

import com.google.gson.Gson

fun Any.toJson() = Gson().toJson(this)
inline fun <reified T : Any> String.fromJson(): T = Gson().fromJson(this, T::class.java)
