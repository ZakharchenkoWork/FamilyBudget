package com.faigenbloom.famillyspandings.id

import java.util.UUID

class IdGenerator {
    fun checkOrGenId(oldId: String = "") = oldId.ifBlank {
        UUID.randomUUID().toString()
    }
}
