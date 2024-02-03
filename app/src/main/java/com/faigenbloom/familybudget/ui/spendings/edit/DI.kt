package com.faigenbloom.familybudget.ui.spendings.edit

import com.faigenbloom.familybudget.domain.details.GetSpendingDetailsByIdUseCase
import com.faigenbloom.familybudget.domain.details.SaveDetailsUseCase
import com.faigenbloom.familybudget.domain.spendings.GetSpendingUseCase
import com.faigenbloom.familybudget.domain.spendings.SaveSpendingUseCase
import com.faigenbloom.familybudget.ui.spendings.mappers.DetailsMapper
import com.faigenbloom.familybudget.ui.spendings.mappers.SpendingsMapper
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
