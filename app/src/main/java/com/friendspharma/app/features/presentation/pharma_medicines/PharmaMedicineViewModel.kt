package com.friendspharma.app.features.presentation.pharma_medicines

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.focus.FocusManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friendspharma.app.core.util.Async
import com.friendspharma.app.features.ScreenArgs
import com.friendspharma.app.features.data.remote.entity.ProductAdd
import com.friendspharma.app.features.data.remote.entity.ProductRemove
import com.friendspharma.app.features.data.remote.model.AllCompanyDto
import com.friendspharma.app.features.data.remote.model.AllCompanyDtoItem
import com.friendspharma.app.features.data.remote.model.CartInfoDto
import com.friendspharma.app.features.data.remote.model.CartInfoDtoItem
import com.friendspharma.app.features.data.remote.model.CategoryProducts
import com.friendspharma.app.features.data.remote.model.OrderDetailsDtoItem
import com.friendspharma.app.features.data.remote.model.ProductsDtoItem
import com.friendspharma.app.features.domain.services.SharedPreferenceHelper
import com.friendspharma.app.features.domain.use_case.GetAllCompanyUseCase
import com.friendspharma.app.features.domain.use_case.GetCartInfoUseCase
import com.friendspharma.app.features.domain.use_case.GetProductsByCompanyUseCase
import com.friendspharma.app.features.domain.use_case.GetProductsUseCase
import com.friendspharma.app.features.domain.use_case.ProductAddUseCase
import com.friendspharma.app.features.domain.use_case.ProductRemoveUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class PharmaMedicineViewModel @Inject constructor(
    private val getProductsByCompanyUseCase: GetProductsByCompanyUseCase,
    private val getCartInfoUseCase: GetCartInfoUseCase,
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val productAddUseCase: ProductAddUseCase,
    private val productRemoveUseCase: ProductRemoveUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val getAllCompanyUseCase: GetAllCompanyUseCase,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val _state = MutableStateFlow(PharmaMedicineState())
    val state: StateFlow<PharmaMedicineState> = _state.asStateFlow()

    private val companyData: String = checkNotNull(savedStateHandle[ScreenArgs.DATA])
    private val company: AllCompanyDtoItem =
        Gson().fromJson(companyData, AllCompanyDtoItem::class.java)

    init {
        getProductsByCompany(company)
        getCartInfo()
        getAllCompanies()
    }

    private fun getAllCompanies() {
        getAllCompanyUseCase.invoke().onEach { result ->
            when(result){
                is Async.Success<*> -> {
                    _state.update { it.copy(isLoading = false, companies = result.data ?: AllCompanyDto()) }
                }

                is Async.Error<*> -> {

                }
                is Async.Loading<*> -> {}
            }
        }.launchIn(viewModelScope)
    }


    private fun getProductsByCompany(company: AllCompanyDtoItem) {
        _state.update { it.copy(company = company) }
        getProductsByCompanyUseCase.invoke(state.value.company.PID_COMPANY ?: 0).onEach { result ->
            when (result) {
                is Async.Success -> {

                    val map: HashMap<String, ArrayList<ProductsDtoItem>> = HashMap()

                    result.data?.map {
                        val categoryName = it.CATEGORY_NAME
                        if (!categoryName.isNullOrEmpty()) {
                            val items = map.getOrDefault(categoryName, arrayListOf())
                            items.add(it)
                            map[categoryName] = items
                        }
                    }

                    val products: ArrayList<CategoryProducts> = arrayListOf()
                    for (key in map.keys) {
                        products.add(CategoryProducts(key, map.getValue(key)))
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            allProduct = products,
                            allSearchedProduct = products
                        )
                    }
                }

                is Async.Error -> {}
                is Async.Loading -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun searchChanged(text: String, focusManager: FocusManager) {
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

    fun updateCurrentItem(item: ProductsDtoItem) {
        _state.update { it.copy(currentItem = item) }
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

                is Async.Error -> {}
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

    fun switch() {
        _state.update { it.copy(isBox = !state.value.isBox) }
    }

    suspend fun categorySelected(index: Int, scrollSate: LazyGridState) {
        if (index == state.value.currentCategoryIndex) {
            _state.update { it.copy(currentCategoryIndex = -1) }
        } else {
            _state.update { it.copy(currentCategoryIndex = index) }
            scrollSate.animateScrollToItem(0)
        }
    }

    suspend fun companySelected(company: AllCompanyDtoItem, scrollSate: LazyGridState) {
        scrollSate.animateScrollToItem(0)
        getProductsByCompany(company)
    }
}