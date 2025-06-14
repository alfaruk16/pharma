package com.friendspharma.app.features.presentation.home.comonents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.friendspharma.app.core.theme.BackGroundDark
import com.friendspharma.app.core.theme.DeepGreen
import com.friendspharma.app.core.theme.Primary
import com.friendspharma.app.core.theme.TealColor
import com.friendspharma.app.features.data.remote.model.CartInfoDto
import com.friendspharma.app.features.data.remote.model.ProductsDtoItem

@Composable
fun ProductItem(
    item: ProductsDtoItem,
    cartIds: Set<Int>,
    cartInfo: CartInfoDto,
    onTap: (ProductsDtoItem) -> Unit,
    addToCart: (ProductsDtoItem) -> Unit,
    removeFromCart: (ProductsDtoItem, String) -> Unit,
    increaseCartItem: (ProductsDtoItem, Int, String) -> Unit,
    height: Dp,
    width: Dp,
    isBox: Boolean,
    addToCartLoading: String
) {
    val inStock = remember {
        mutableStateOf((item.STOCK_QTY_BOX ?: 0.0) > 0.0)
    }

    Card(
        modifier = Modifier
            .padding(5.dp)
            .clickable { onTap(item) },
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                AsyncImage(
                    model = item.IMAGE_URL,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                        .height(height)
                        .width(width)
                )

                Row(modifier = Modifier.padding(5.dp)) {
                    Text(
                        text = (if (isBox) item.BOX_SALES_PER.toString() else item.LEAF_SALES_PER.toString()) + "%",
                        fontWeight = FontWeight.Bold,
                        color = TealColor,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .rotate(315f)
                            .padding(top = 10.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Save " + (if (isBox) item.BOX_OFFER_VALUE.toString() else item.LEAF_OFFER_VALUE.toString()) + "৳",
                        color = DeepGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

            }
            Column(modifier = Modifier.padding(5.dp)) {
                Row {
                    Text(
                        text = item.PRODUCT_NAME ?: "",
                        color = Color.Black,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(10.dp))


                }
                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = (if (isBox) item.BOX_MRP_PRICE.toString() else item.LEAF_MRP_PRICE.toString()) + "৳",
                            textDecoration = TextDecoration.LineThrough,
                            color = DeepGreen,
                            fontWeight = FontWeight.W500
                        )
                        Text(
                            text = (if (isBox) item.BOX_SALES_PRICE.toString() else item.BOX_SALES_PRICE.toString()) + "৳",
                            color = TealColor,
                            fontWeight = FontWeight.W500
                        )
                    }
                    if (cartIds.contains(item.PID_PRODUCT) && inStock.value)
                        IncreaseDecrease(
                            item = item,
                            removeFromCart = removeFromCart,
                            increaseCartItem = increaseCartItem,
                            cartInfo = cartInfo,
                            addToCartLoading = addToCartLoading
                        )
                    else if (inStock.value)
                        Row {
                            Box(
                                modifier = Modifier
                                    .background(
                                        BackGroundDark, shape = CircleShape
                                    )
                                    .clickable {
                                        addToCart(item)
                                    }
                                    .size(30.dp)
                            ) {
                                if (item.PID_PRODUCT.toString() == addToCartLoading)
                                    CircularProgressIndicator(
                                        modifier = Modifier.padding(5.dp),
                                        color = Primary,
                                        strokeWidth = 3.dp
                                    )
                                else
                                    Icon(
                                        Icons.Filled.AddShoppingCart,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .size(20.dp)
                                    )
                            }
                        }
                }
            }

        }
    }
}