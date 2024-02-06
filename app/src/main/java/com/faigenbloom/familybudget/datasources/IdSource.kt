package com.faigenbloom.familybudget.datasources

import java.util.EnumMap

class IdSource {
    private val ids: EnumMap<ID, String> = EnumMap<ID, String>(ID::class.java)
    operator fun get(key: ID): String {
        return ids[key] ?: ""
    }

    operator fun set(key: ID, value: String) {
        ids[key] = value
    }
}

enum class ID {
    FAMILY,
    USER,
}

