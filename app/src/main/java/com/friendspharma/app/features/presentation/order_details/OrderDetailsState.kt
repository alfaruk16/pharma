package com.friendspharma.app.features.presentation.order_details

import com.friendspharma.app.features.data.remote.model.OrderDetailsDto

data class OrderDetailsState(
    val isLoading: Boolean = true,
    val orders: OrderDetailsDto = OrderDetailsDto(),
    val totalQuantity: Int = 0,
    val totalPrice: Double = 0.0,
    val totalAmount: Double = 0.0,
    val discount: Double = 0.0
)
