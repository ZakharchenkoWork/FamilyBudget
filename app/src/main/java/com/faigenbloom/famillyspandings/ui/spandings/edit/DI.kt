package com.faigenbloom.famillyspandings.ui.spandings.edit

import com.faigenbloom.famillyspandings.domain.SaveSpendingUseCase
import com.faigenbloom.famillyspandings.domain.details.GetSpendingDetailsByIdUseCase
import com.faigenbloom.famillyspandings.domain.details.SaveDetailsUseCase
import com.faigenbloom.famillyspandings.domain.spendings.GetSpendingUseCase
import com.faigenbloom.famillyspandings.ui.spandings.DetailsMapper
import com.faigenbloom.famillyspandings.ui.spandings.SpendingsMapper
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val spendingEditModule = module {
    viewModelOf(::SpendingEditViewModel)
    singleOf(::SpendingsMapper)
    singleOf(::DetailsMapper)
    single {
        SaveSpendingUseCase(
            idGeneratorUseCase = get(),
            spendingsRepository = get(),
            spendingsMapper = get<SpendingsMapper>(),
        )
    }
    single {
        SaveDetailsUseCase(
            idGeneratorUseCase = get(),
            detailsRepository = get(),
            getSpendingDetailsUseCase = get(),
            detailsMapper = get<DetailsMapper>(),
        )
    }
    single { GetSpendingUseCase(get(), get<SpendingsMapper>()) }
    single { GetSpendingDetailsByIdUseCase(get(), get<DetailsMapper>()) }
}
