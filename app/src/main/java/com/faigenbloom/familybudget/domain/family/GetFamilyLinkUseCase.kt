package com.faigenbloom.familybudget.domain.family

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFamilyLinkUseCase {
    suspend operator fun invoke(familyId: String): String {
        return withContext(Dispatchers.Default) {
            "https://faigenbloom.page.link/" +
                    "?link=https://faigenbloom.page.link/familyjoin?" +
                    "familyId=$familyId" +
                    "&apn=com.faigenbloom.familybudget"
        }
    }
}
