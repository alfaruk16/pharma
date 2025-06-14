package com.friendspharma.app.features.presentation.order_details

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.friendspharma.app.core.util.Async
import com.friendspharma.app.features.ScreenArgs
import com.friendspharma.app.features.data.remote.model.OrderDetailsDto
import com.friendspharma.app.features.domain.use_case.GetOrderDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.math.RoundingMode
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val getOrderDetailsUseCase: GetOrderDetailsUseCase,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val _state = MutableStateFlow(OrderDetailsState())
    val state: StateFlow<OrderDetailsState> = _state.asStateFlow()

    private val id: String = checkNotNull(savedStateHandle[ScreenArgs.DATA])

    init {
        getOrder(id)
    }

    private fun getOrder(id: String) {
        getOrderDetailsUseCase.invoke(id).onEach { result ->
            when (result) {
                is Async.Success -> {
                    var totalAmount = 0.0
                    var totalQuantity = 0
                    var totalPrice = 0.0
                    for (item in result.data?.data ?: emptyList()) {
                        totalQuantity += item.QUANTITY ?: 0
                        totalPrice += (item.TOTAL_PRICE ?: 0.0)
                        totalAmount += (item.MRP_PRICE ?: 0.0) * (item.QUANTITY ?: 0)
                    }
                    _state.update {
                        it.copy(
                            isLoading = false, orders = result.data ?: OrderDetailsDto(),
                            totalQuantity = totalQuantity,
                            totalPrice = totalPrice.toBigDecimal()
                                .setScale(2, RoundingMode.HALF_EVEN).toDouble(),
                            totalAmount = totalAmount.toBigDecimal()
                                .setScale(2, RoundingMode.HALF_EVEN).toDouble(),
                            discount = (totalAmount - totalPrice).toBigDecimal().setScale(
                                2,
                                RoundingMode.HALF_EVEN
                            ).toDouble()
                        )
                    }
                }

                is Async.Error -> {}
                is Async.Loading -> {}
            }
        }.launchIn(viewModelScope)
    }


}