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
    singleOf(::SaveSpendingUseCase)
    single {
        SaveDetailsUseCase(
            detailsRepository = get(),
            detailsMapper = get<DetailsMapper>(),
        )
    }
    singleOf(::GetSpendingUseCase)
    single { GetSpendingDetailsByIdUseCase(get(), get<DetailsMapper>()) }
}
