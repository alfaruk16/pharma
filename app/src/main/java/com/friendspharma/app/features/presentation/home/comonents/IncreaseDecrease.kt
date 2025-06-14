package com.friendspharma.app.features.presentation.home.comonents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.friendspharma.app.core.theme.BackGroundDark
import com.friendspharma.app.core.theme.Primary
import com.friendspharma.app.features.data.remote.model.CartInfoDto
import com.friendspharma.app.features.data.remote.model.ProductsDtoItem

@Composable
fun IncreaseDecrease(
    item: ProductsDtoItem,
    removeFromCart: (ProductsDtoItem, String) -> Unit,
    increaseCartItem: (ProductsDtoItem, Int, String) -> Unit,
    cartInfo: CartInfoDto,
    addToCartLoading: String
) {

    val isAdd = remember {
        mutableStateOf(false)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .background(
                    BackGroundDark,
                    shape = RoundedCornerShape(5.dp)
                )
                .size(30.dp)
                .clickable {
                    isAdd.value = false
                    removeFromCart(item, getSalesUnit(item.PID_PRODUCT, cartInfo))
                },
            contentAlignment = Alignment.Center
        ) {
            if (item.PID_PRODUCT.toString() == addToCartLoading && !isAdd.value)
                CircularProgressIndicator(
                    modifier = Modifier.padding(5.dp),
                    color = Primary,
                    strokeWidth = 3.dp
                )
            else
                Text(
                    text = "-", fontSize = 24.sp, fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Box(modifier = Modifier.padding(horizontal = 5.dp)) {
            Text(
                text = getQuantity(
                    item.PID_PRODUCT,
                    cartInfo = cartInfo
                ).toString(), fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Box(
            modifier = Modifier
                .background(
                    BackGroundDark,
                    shape = RoundedCornerShape(5.dp)
                )
                .size(30.dp)
                .clickable {
                    isAdd.value = true
                    increaseCartItem(item, 1, getSalesUnit(item.PID_PRODUCT, cartInfo))
                },
            contentAlignment = Alignment.Center
        ) {
            if (item.PID_PRODUCT.toString() == addToCartLoading && isAdd.value)
                CircularProgressIndicator(
                    modifier = Modifier.padding(5.dp),
                    color = Primary,
                    strokeWidth = 3.dp
                )
            else
                Text(
                    text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
        }
    }
}

fun getSalesUnit(pidProduct: Int?, cartInfo: CartInfoDto): String {
    for (item in cartInfo.data ?: emptyList()) {
        if (pidProduct == item.PID_PRODUCT)
            return item.SALES_UNIT ?: ""
    }
    return ""
}
