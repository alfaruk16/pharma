package com.friendspharma.app.features.presentation.home

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.focus.FocusManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friendspharma.app.core.util.Async
import com.friendspharma.app.features.data.remote.entity.ProductAdd
import com.friendspharma.app.features.data.remote.entity.ProductRemove
import com.friendspharma.app.features.data.remote.model.AllCategoryDto
import com.friendspharma.app.features.data.remote.model.AllCompanyDto
import com.friendspharma.app.features.data.remote.model.CartInfoDto
import com.friendspharma.app.features.data.remote.model.CartInfoDtoItem
import com.friendspharma.app.features.data.remote.model.CategoryProducts
import com.friendspharma.app.features.data.remote.model.ProductsDto
import com.friendspharma.app.features.data.remote.model.ProductsDtoItem
import com.friendspharma.app.features.domain.services.SharedPreferenceHelper
import com.friendspharma.app.features.domain.use_case.GetAllCategoryUseCase
import com.friendspharma.app.features.domain.use_case.GetAllCompanyUseCase
import com.friendspharma.app.features.domain.use_case.GetCartInfoUseCase
import com.friendspharma.app.features.domain.use_case.GetProductsUseCase
import com.friendspharma.app.features.domain.use_case.ProductAddUseCase
import com.friendspharma.app.features.domain.use_case.ProductRemoveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getCartInfoUseCase: GetCartInfoUseCase,
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val productAddUseCase: ProductAddUseCase,
    private val productRemoveUseCase: ProductRemoveUseCase,
    private val getAllCompanyUseCase: GetAllCompanyUseCase,
    private val getAllCategoryUseCase: GetAllCategoryUseCase
) :
    ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun init() {
        getCartInfo()
        getAllCompanies()
        getAllCategories()
    }

    private fun getAllCategories() {
        getAllCategoryUseCase.invoke().onEach { result ->
            when (result) {
                is Async.Success<*> -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            categories = result.data ?: AllCategoryDto()
                        )
                    }
                    getProducts()
                }

                is Async.Error<*> -> {

                }

                is Async.Loading<*> -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun getAllCompanies() {
        getAllCompanyUseCase.invoke().onEach { result ->
            when (result) {
                is Async.Success<*> -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            companies = result.data ?: AllCompanyDto()
                        )
                    }
                }

                is Async.Error<*> -> {

                }

                is Async.Loading<*> -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun getProducts() {
        getProductsUseCase.invoke().onEach { result ->
            when (result) {
                is Async.Success -> {

                    val products: ArrayList<CategoryProducts> = arrayListOf()

                    for (category in state.value.categories.data ?: emptyList()) {
                        products.add(
                            CategoryProducts(
                                category.CATEGORY_NAME ?: "",
                                result.data?.data?.filter {
                                    it.PID_CATEGORY == category.PID_CATEGORY
                                } ?: emptyList()))
                    }

                    if (state.value.isSortedeByCategory) {
                        _state.update {
                            it.copy(
                                products = result.data ?: ProductsDto(),
                                allProduct = products,
                                allSearchedProduct = products,
                                isLoading = false,
                                currentCategoryIndex = -1
                            )
                        }
                    } else {
                        sortByPharmaceutical()
                    }
                }

                is Async.Error -> {}

                is Async.Loading -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun updateCurrentItem(item: ProductsDtoItem) {
        _state.update { it.copy(currentItem = item) }
    }

    fun searchChanged(text: String, focusManager: FocusManager) {
        viewModelScope.launch {
            _state.update { it.copy(search = text) }
            if (text.isNotEmpty()) {
                val items: ArrayList<CategoryProducts> = arrayListOf()
                for (item in state.value.allProduct) {
                    val products = item.data?.filter {
                        it.COMPANY_NAME?.lowercase()?.contains(text.lowercase()) == true ||
                                it.CATEGORY_NAME?.lowercase()?.contains(text.lowercase()) == true ||
                                it.PRODUCT_NAME?.lowercase()?.contains(text.lowercase()) == true
                    }
                    if ((products?.size ?: 0) > 0) {
                        items.add(CategoryProducts(item.category, products))
                    }
                }
                _state.update { it.copy(allSearchedProduct = items) }
            } else {
                focusManager.clearFocus()
                _state.update { it.copy(allSearchedProduct = state.value.allProduct) }
            }
        }
    }

    fun addToCart(product: ProductsDtoItem, quantity: Int, salesUnit: String, context: Context) {
        productAddUseCase.invoke(
            ProductAdd(
                mobile_no = sharedPreferenceHelper.getUser().MOBILE_NO ?: "",
                pid_product = product.PID_PRODUCT.toString(),
                pqty = quantity.toString(),
                salesunit = salesUnit
            )
        ).onEach { result ->
            when (result) {
                is Async.Success -> {
                    getCartInfo()
                    Toast.makeText(context, result.data?.message ?: "", Toast.LENGTH_SHORT).show()
                }

                is Async.Error -> {
                    _state.update { it.copy(addToCartLoading = "") }
                    Toast.makeText(
                        context,
                        result.data?.message ?: "An unknown error occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Async.Loading -> {
                    _state.update { it.copy(addToCartLoading = product.PID_PRODUCT.toString()) }
                }
            }

        }.launchIn(viewModelScope)
    }

    fun removeFromCart(item: ProductsDtoItem, salesUnit: String, context: Context) {
        val product = getProductFromCart(item.PID_PRODUCT)
        productRemoveUseCase.invoke(
            ProductRemove(
                sharedPreferenceHelper.getUser().MOBILE_NO ?: "",
                pid_product = product.PID_PRODUCT.toString(),
                pid_tran_dtl = product.PID_TRAN_DTL.toString(),
                salesunit = salesUnit
            )
        ).onEach { result ->
            when (result) {
                is Async.Success -> {
                    getCartInfo()
                    Toast.makeText(context, result.data?.message ?: "", Toast.LENGTH_SHORT).show()
                }

                is Async.Error -> {}
                is Async.Loading -> {
                    _state.update { it.copy(addToCartLoading = item.PID_PRODUCT.toString()) }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getProductFromCart(pidProduct: Int?): CartInfoDtoItem {
        for (item in state.value.cartInfo.data ?: emptyList()) {
            if (pidProduct == item.PID_PRODUCT)
                return item
        }
        return CartInfoDtoItem()
    }

    private fun getCartInfo() {
        getCartInfoUseCase.invoke(sharedPreferenceHelper.getUser().MOBILE_NO ?: "")
            .onEach { result ->
                when (result) {
                    is Async.Success -> {
                        var quantity = 0
                        val set = HashSet<Int>()
                        for (item in result.data?.data ?: emptyList()) {
                            quantity += item.QUANTITY?.toInt() ?: 0
                            set.add(item.PID_PRODUCT ?: -1)
                        }
                        _state.update {
                            it.copy(
                                cartInfo = result.data ?: CartInfoDto(),
                                cartItemQuantity = quantity,
                                cartIds = set,
                                addToCartLoading = ""
                            )
                        }
                    }

                    is Async.Error -> {}
                    is Async.Loading -> {}
                }

            }.launchIn(viewModelScope)
    }

    fun categorySelected(index: Int) {
        if (index == state.value.currentCategoryIndex) {
            _state.update { it.copy(currentCategoryIndex = -1) }
        } else {
            _state.update { it.copy(currentCategoryIndex = index) }
        }
    }

    fun switch() {
        _state.update { it.copy(isBox = !state.value.isBox) }
    }

    fun productsByBox(isBox: Boolean) {
        _state.update { it.copy(isBox = isBox) }
    }

    suspend fun sortByPharmaceutical(scrollSate: LazyGridState? = null) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val products: ArrayList<CategoryProducts> = arrayListOf()

            for (pharma in state.value.companies.data ?: emptyList()) {
                products.add(
                    CategoryProducts(
                        pharma.COMPANY_NAME ?: "",
                        state.value.products.data?.filter {
                            it.PID_COMPANY == pharma.PID_COMPANY
                        } ?: emptyList()))
            }

            _state.update {
                it.copy(
                    allProduct = products,
                    allSearchedProduct = products,
                    isLoading = false,
                    currentCategoryIndex = -1,
                    isSortedeByCategory = false
                )
            }
        }
        scrollSate?.animateScrollToItem(0)
    }

    suspend fun sortByCategory(scrollSate: LazyGridState) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val products: ArrayList<CategoryProducts> = arrayListOf()

            for (category in state.value.categories.data ?: emptyList()) {
                products.add(
                    CategoryProducts(
                        category.CATEGORY_NAME ?: "",
                        state.value.products.data?.filter {
                            it.PID_CATEGORY == category.PID_CATEGORY
                        } ?: emptyList()))
            }

            _state.update {
                it.copy(
                    allProduct = products,
                    allSearchedProduct = products,
                    isLoading = false,
                    currentCategoryIndex = -1,
                    isSortedeByCategory = true
                )
            }
        }
        scrollSate.animateScrollToItem(0)
    }
}