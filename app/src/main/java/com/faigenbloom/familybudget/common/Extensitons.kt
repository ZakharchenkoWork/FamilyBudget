package com.faigenbloom.familybudget.common

import androidx.paging.compose.LazyPagingItems

fun <T : Any> LazyPagingItems<T>.isEmpty() = this.itemCount == 0
