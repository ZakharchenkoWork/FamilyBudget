package com.faigenbloom.familybudget.domain

import java.util.UUID

class GenerateIdUseCase {
    operator fun invoke(oldId: String = "") = oldId.ifBlank {
        UUID.randomUUID().toString() + System.currentTimeMillis()
    }
}
