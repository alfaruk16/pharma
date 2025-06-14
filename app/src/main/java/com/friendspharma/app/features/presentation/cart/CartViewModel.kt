package com.friendspharma.app.features.presentation.cart

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friendspharma.app.core.util.Async
import com.friendspharma.app.features.data.remote.entity.ChangeAddress
import com.friendspharma.app.features.data.remote.entity.DeleteAddress
import com.friendspharma.app.features.data.remote.entity.InsertAddress
import com.friendspharma.app.features.data.remote.entity.ProductAdd
import com.friendspharma.app.features.data.remote.entity.ProductRemove
import com.friendspharma.app.features.data.remote.entity.SubmitOrder
import com.friendspharma.app.features.data.remote.model.AddressDto
import com.friendspharma.app.features.data.remote.model.AddressDtoItem
import com.friendspharma.app.features.data.remote.model.CartInfoDto
import com.friendspharma.app.features.data.remote.model.CartInfoDtoItem
import com.friendspharma.app.features.data.remote.model.ProductsDtoItem
import com.friendspharma.app.features.domain.services.SharedPreferenceHelper
import com.friendspharma.app.features.domain.use_case.ChangeAddressUseCase
import com.friendspharma.app.features.domain.use_case.DeleteAddressUseCase
import com.friendspharma.app.features.domain.use_case.GetAddressUseCase
import com.friendspharma.app.features.domain.use_case.GetCartInfoUseCase
import com.friendspharma.app.features.domain.use_case.InsertAddressUseCase
import com.friendspharma.app.features.domain.use_case.ProductAddUseCase
import com.friendspharma.app.features.domain.use_case.ProductRemoveUseCase
import com.friendspharma.app.features.domain.use_case.SubmitOrderUseCase
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
class CartViewModel @Inject constructor(
    private val cartInfoUseCase: GetCartInfoUseCase,
    private val sharedPreferenceHelper: SharedPreferenceHelper,
    private val productAddUseCase: ProductAddUseCase,
    private val productRemoveUseCase: ProductRemoveUseCase,
    private val submitOrderUseCase: SubmitOrderUseCase,
    private val getAddressUseCase: GetAddressUseCase,
    private val preferenceHelper: SharedPreferenceHelper,
    private val insertAddressUseCase: InsertAddressUseCase,
    private val changeAddressUseCase: ChangeAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) :
    ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state.asStateFlow()

    init {
        getCartInfo()
        getAddress()
    }

    private fun getAddress() {
        getAddressUseCase.invoke(preferenceHelper.getUser().USER_ID.toString()).onEach { result ->
            when (result) {
                is Async.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            addresses = result.data ?: AddressDto(),
                            addressLoading = false
                        )
                    }
                }

                is Async.Error -> _state.update { it.copy(isLoading = false) }
                is Async.Loading -> _state.update { it.copy(isLoading = true) }
            }
        }.launchIn(viewModelScope)
    }

    private fun getCartInfo() {
        cartInfoUseCase.invoke(sharedPreferenceHelper.getUser().MOBILE_NO ?: "").onEach { result ->
            when (result) {
                is Async.Success -> {
                    var totalQuantity = 0
                    var totalPrice = 0.0
                    for (item in result.data?.data ?: emptyList()) {
                        totalQuantity += item.QUANTITY?.toInt() ?: 0
                        totalPrice += item.TOTAL_PRICE ?: 0.0
                    }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            cartInfoDto = result.data ?: CartInfoDto(),
                            totalQuantity = totalQuantity,
                            totalPrice = totalPrice,
                            addToCartLoading = ""
                        )
                    }
                }

                is Async.Error -> {}
                is Async.Loading -> {}
            }
        }.launchIn(viewModelScope)
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
                    _state.update { it.copy(addToCartLoading = product.PID_PRODUCT.toString()) }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getProductFromCart(pidProduct: Int?): CartInfoDtoItem {
        for (item in state.value.cartInfoDto.data ?: emptyList()) {
            if (pidProduct == item.PID_PRODUCT)
                return item
        }
        return CartInfoDtoItem()
    }

    fun submitOrder(context: Context, pop: () -> Unit) {
        if (state.value.totalPrice < 500) {
            Toast.makeText(context, "Total Price must be at least 500", Toast.LENGTH_SHORT).show()
            return
        }
        if (!state.value.selectedAddress.ADDRESS.isNullOrEmpty()) {
            submitOrderUseCase.invoke(
                submitOrder = SubmitOrder(
                    mobile_no = sharedPreferenceHelper.getUser().MOBILE_NO ?: "",
                    pid_tran_mst = state.value.cartInfoDto.data?.get(0)?.PID_TRAN_MST ?: "",
                    address = state.value.selectedAddress.ADDRESS ?: ""
                )
            )
                .onEach { result ->
                    when (result) {
                        is Async.Success -> {
                            Toast.makeText(context, result.data?.message ?: "", Toast.LENGTH_SHORT)
                                .show()
                            pop()
                        }

                        is Async.Error -> {}
                        is Async.Loading -> {}
                    }
                }.launchIn(viewModelScope)
        } else {
            _state.update { it.copy(showAddressDialog = true) }
        }
    }

    fun closeAddressDialog() {
        _state.update { it.copy(showAddressDialog = false) }
    }

    fun insertAddress(item: AddressDtoItem) {
        insertAddressUseCase.invoke(
            InsertAddress(
                userId = preferenceHelper.getUser().USER_ID.toString(),
                address = item.ADDRESS ?: "",
                addrType = item.ADDR_TYPE ?: ""
            )
        ).onEach { result ->
            when (result) {
                is Async.Success -> {
                    getAddress()
                }

                is Async.Error -> {}
                is Async.Loading -> {
                    _state.update { it.copy(addressLoading = true) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addressSelected(item: AddressDtoItem) {
        _state.update { it.copy(selectedAddress = item) }
    }

    fun changeAddress(item: ChangeAddress) {
        changeAddressUseCase.invoke(item).onEach { result ->
            when (result) {
                is Async.Success -> {
                    if (state.value.selectedAddress.PID == item.pid) {
                        _state.update {
                            it.copy(
                                selectedAddress = AddressDtoItem(
                                    PID = item.pid,
                                    ADDRESS = item.address,
                                    ADDR_TYPE = item.addrType
                                )
                            )
                        }
                    }
                    getAddress()
                }

                is Async.Error -> {}
                is Async.Loading -> {
                    _state.update { it.copy(addressLoading = true) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteAddress(item: DeleteAddress) {
        deleteAddressUseCase.invoke(item).onEach { result ->
            when (result) {
                is Async.Success -> {
                    if (item.pid == state.value.selectedAddress.PID) {
                        _state.update { it.copy(selectedAddress = AddressDtoItem()) }
                    }
                    getAddress()
                }

                is Async.Error -> {}
                is Async.Loading -> {
                    _state.update { it.copy(addressLoading = true) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun showAddressDialog() {
        _state.update { it.copy(showAddressDialog = true) }
    }

}
