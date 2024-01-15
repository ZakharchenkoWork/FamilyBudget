package com.faigenbloom.famillyspandings.id

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val idGeneratorModule = module {
    singleOf(::IdGenerator)
}
