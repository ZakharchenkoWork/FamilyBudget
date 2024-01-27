package com.faigenbloom.famillyspandings.ui.spendings.show

import com.faigenbloom.famillyspandings.domain.categories.GetCategoryByIdUseCase
import com.faigenbloom.famillyspandings.domain.details.GetSpendingDetailsByIdUseCase
import com.faigenbloom.famillyspandings.domain.details.SaveDetailsUseCase
import com.faigenbloom.famillyspandings.domain.spendings.GetSpendingUseCase
import com.faigenbloom.famillyspandings.domain.spendings.SaveSpendingUseCase
import com.faigenbloom.famillyspandings.ui.categories.CategoriesMapper
import com.faigenbloom.famillyspandings.ui.spendings.mappers.DetailsMapper
import com.faigenbloom.famillyspandings.ui.spendings.mappers.SpendingsMapper
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val spendingShowModule = module {
    viewModelOf(::SpendingShowViewModel)
    singleOf(::SpendingsMapper)
    singleOf(::DetailsMapper)
    singleOf(::CategoriesMapper)
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

    single { GetCategoryByIdUseCase(get(), get<CategoriesMapper>()) }
}
