package com.friendspharma.app.features.presentation.pharma

import com.friendspharma.app.core.util.Common
import com.friendspharma.app.features.data.remote.model.AllCompanyDto
import com.friendspharma.app.features.data.remote.model.AllCompanyDtoItem
import com.friendspharma.app.features.data.remote.model.CategoryProducts


data class PharmaState(
    val isLoading: Boolean = true,
    val cartItemQuantity: Int = 0,
    val search: String = "",
    val companies: AllCompanyDto = AllCompanyDto(),
    val allSearchedCompanies: AllCompanyDto = AllCompanyDto(),
)
