package com.faigenbloom.familybudget.domain

import com.faigenbloom.familybudget.datasources.REPEAT_INFIX
import java.util.UUID

class GenerateIdUseCase {
    operator fun invoke(oldId: String = ""): String {
        if (oldId.contains(REPEAT_INFIX)) {
            return UUID.randomUUID().toString() + System.currentTimeMillis()
        }

        return oldId.ifBlank {
            UUID.randomUUID().toString() + System.currentTimeMillis()
        }
    }
}
