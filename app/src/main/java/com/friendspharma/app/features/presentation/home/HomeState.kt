package com.friendspharma.app.features.presentation.home

import com.friendspharma.app.core.util.Common
import com.friendspharma.app.features.data.remote.model.AllCategoryDto
import com.friendspharma.app.features.data.remote.model.AllCompanyDto
import com.friendspharma.app.features.data.remote.model.CartInfoDto
import com.friendspharma.app.features.data.remote.model.CategoryProducts
import com.friendspharma.app.features.data.remote.model.ProductsDto
import com.friendspharma.app.features.data.remote.model.ProductsDtoItem


data class HomeState(
    val isLoading: Boolean = true,
    val currentItem: ProductsDtoItem = ProductsDtoItem(),
    val products: ProductsDto = ProductsDto(),
    val allProduct: ArrayList<CategoryProducts> = arrayListOf(),
    val allSearchedProduct: ArrayList<CategoryProducts> = arrayListOf(),
    val search: String = "",
    val cartInfo: CartInfoDto = CartInfoDto(),
    val cartItemQuantity: Int = 0,
    val cartIds: Set<Int> = HashSet(),
    val currentCategoryIndex: Int = -1,
    val isBox: Boolean = true,
    val addToCartLoading: String = "",
    val isSortedeByCategory: Boolean = true,
    val banners: HashMap<String, Int> = Common.bannersMap,
    val companyMap: HashMap<String, Int> = Common.companyMap,
    val companies: AllCompanyDto = AllCompanyDto(),
    val categories: AllCategoryDto = AllCategoryDto()
)
