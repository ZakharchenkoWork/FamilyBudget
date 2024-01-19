package com.faigenbloom.famillyspandings.domain.details

import android.util.Log
import com.faigenbloom.famillyspandings.comon.Identifiable
import com.faigenbloom.famillyspandings.comon.Mapper
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.domain.GenerateIdUseCase
import com.faigenbloom.famillyspandings.repositories.DetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveDetailsUseCase<T : Identifiable>(
    private val idGeneratorUseCase: GenerateIdUseCase,
    private val detailsRepository: DetailsRepository,
    private val getSpendingDetailsUseCase: GetSpendingDetailsByIdUseCase<T>,
    private val detailsMapper: Mapper<T, SpendingDetailEntity>,
) {
    suspend operator fun invoke(
        spendingId: String,
        details: List<T>,
    ) {
        withContext(Dispatchers.IO) {
            val oldSpendingDetails = getSpendingDetailsUseCase.invoke(spendingId)
            oldSpendingDetails.forEach { oldDetail ->
                val newDetail = details.firstOrNull { newDetail ->
                    oldDetail.id == newDetail.id
                }

                val oldDetailCrossRefsList = detailsRepository.getDetailCrossRefs(oldDetail.id)
                newDetail?.let {
                    if (oldDetailCrossRefsList.size == 1) {
                        //it will replace old one
                        detailsRepository.addSpendingDetail(detailsMapper.forDB(newDetail))
                    } else {
                        detailsRepository.getSpendingDetailDuplicate(detailsMapper.forDB(newDetail))
                            ?.let { duplicateDetail ->
                                detailsRepository.replaceCrossRef(
                                    spendingId,
                                    oldDetail.id,
                                    duplicateDetail.id,
                                )
                            } ?: kotlin.run {
                            val newDetailNewId = idGeneratorUseCase()
                            detailsRepository.addSpendingDetail(
                                detailsMapper.forDB(
                                    detailsMapper.copyChangingId(
                                        newDetail,
                                        newDetailNewId,
                                    ),
                                ),
                            )
                            detailsRepository.replaceCrossRef(
                                spendingId,
                                oldDetail.id,
                                newDetailNewId,
                            )
                        }
                    }
                } ?: kotlin.run {
                    if (oldDetailCrossRefsList.size == 1) {
                        detailsRepository.deleteSpendingDetail(oldDetail.id)
                    }
                    detailsRepository.deleteCrossRef(
                        spendingId = spendingId,
                        detailId = oldDetail.id,
                    )
                }
            }


            details.forEach {
                Log.d("ID", it.id)
                val newDetail = detailsMapper.copyChangingId(it, idGeneratorUseCase(it.id))
                oldSpendingDetails.firstOrNull { oldDetail ->
                    oldDetail.id == newDetail.id
                } ?: kotlin.run {
                    detailsRepository.addNewDetail(detailsMapper.forDB(newDetail), spendingId)
                }
            }
        }
    }
}
