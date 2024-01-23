package com.faigenbloom.famillyspandings.common

abstract class Mapper<T : Identifiable, V> {
    abstract fun forUI(entity: V): T
    abstract fun forDB(model: T): V
    abstract fun copyChangingId(model: T, id: String): T
}
