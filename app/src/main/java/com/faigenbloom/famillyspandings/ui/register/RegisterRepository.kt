package com.faigenbloom.famillyspandings.ui.register

import com.faigenbloom.famillyspandings.datasources.BaseDataSource

class RegisterRepository(private val dataSource: BaseDataSource) {
    suspend fun register(
        name: String,
        familyName: String,
        email: String,
        password: String,
    ): Boolean {
        return dataSource.register(name, familyName, email, password)
    }
}
