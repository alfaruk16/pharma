package com.friendspharma.app.features.presentation.home.comonents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.friendspharma.app.core.theme.BackGroundDark
import com.friendspharma.app.core.theme.DeepGreen
import com.friendspharma.app.core.theme.Primary
import com.friendspharma.app.core.theme.TealColor
import com.friendspharma.app.features.data.remote.model.CartInfoDto
import com.friendspharma.app.features.data.remote.model.ProductsDtoItem

@Composable
fun ProductDetails(
    item: ProductsDtoItem,
    cartIds: Set<Int> = HashSet(),
    cartInfo: CartInfoDto,
    addToCart: (ProductsDtoItem) -> Unit,
    removeFromCart: (ProductsDtoItem, String) -> Unit,
    increaseCartItem: (ProductsDtoItem, Int, String) -> Unit,
    addToCartLoading: String,
    onDismiss: () -> Unit
) {

    val width = LocalConfiguration.current.screenWidthDp.dp
    val inStock = remember {
        mutableStateOf((item.STOCK_QTY_BOX ?: 0.0) > 0.0)
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(Modifier.padding(13.dp)) {
                Column(
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        AsyncImage(
                            model = item.IMAGE_URL,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(0.dp, width / 2)
                                .clip(RoundedCornerShape(15.dp))
                        )
                        Text(
                            text = if (inStock.value) "In Stock left (${item.STOCK_QTY_BOX?.toInt()})" else "Out of Stock",
                            fontWeight = FontWeight.Bold,
                            color = if (inStock.value) Color.Black else Color.Red,
                            modifier = Modifier.padding(5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = item.PRODUCT_NAME ?: "",
                        color = Color.Black,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Box Size: " + item.BOX_SIZE_TITLE)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = item.COMPANY_NAME ?: "")
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Box(contentAlignment = Alignment.TopCenter) {
                            Card(
                                elevation = CardDefaults.cardElevation(5.dp),
                                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(Color.White)
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = "MRP " + item.BOX_MRP_PRICE.toString() + "৳",
                                        textDecoration = TextDecoration.LineThrough,
                                        color = DeepGreen,
                                        fontWeight = FontWeight.W500
                                    )
                                    Text(
                                        text = "BDT- " + item.BOX_SALES_PRICE.toString() + "৳",
                                        fontWeight = FontWeight.W500,
                                        color = TealColor
                                    )

                                    Text(
                                        text = "Offer-" + item.BOX_SALES_PER + "%",
                                        fontWeight = FontWeight.W500,
                                        color = TealColor
                                    )
                                    Text(
                                        text = "Save " + item.BOX_OFFER_VALUE.toString() + "৳",
                                        color = DeepGreen,
                                        fontWeight = FontWeight.W500
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier.background(
                                    BackGroundDark,
                                    shape = CircleShape
                                )
                            ) {
                                Text(
                                    text = "Box", color = Primary, fontWeight = FontWeight.W500,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(contentAlignment = Alignment.TopCenter) {
                            Card(
                                elevation = CardDefaults.cardElevation(5.dp),
                                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(Color.White)
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = "MRP " + item.LEAF_MRP_PRICE.toString() + "৳",
                                        textDecoration = TextDecoration.LineThrough,
                                        color = DeepGreen,
                                        fontWeight = FontWeight.W500
                                    )
                                    Text(
                                        text = "BDT- " + item.LEAF_SALES_PER.toString() + "৳",
                                        fontWeight = FontWeight.W500,
                                        color = TealColor
                                    )

                                    Text(
                                        text = "Offer-" + item.LEAF_SALES_PER + "%",
                                        fontWeight = FontWeight.W500,
                                        color = TealColor
                                    )
                                    Text(
                                        text = "Save " + item.LEAF_OFFER_VALUE.toString() + "৳",
                                        color = DeepGreen,
                                        fontWeight = FontWeight.W500
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier.background(
                                    BackGroundDark,
                                    shape = RoundedCornerShape(20.dp)
                                )
                            ) {
                                Text(
                                    text = "Leaf",
                                    color = Primary,
                                    fontWeight = FontWeight.W500,
                                    modifier = Modifier
                                        .padding(5.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
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
            Box(
                Modifier
                    .background(BackGroundDark, CircleShape)
                    .clickable { onDismiss() }) {
                Icon(
                    Icons.Default.Close, contentDescription = null,
                    Modifier.padding(5.dp)
                )
            }
        }
    }
}