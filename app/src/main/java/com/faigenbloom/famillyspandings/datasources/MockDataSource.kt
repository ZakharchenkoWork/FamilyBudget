package com.faigenbloom.famillyspandings.datasources

class MockDataSource : BaseDataSource {
    override suspend fun login(login: String, password: String): Boolean {
        return login == "a" && password == "a"
    }
}
