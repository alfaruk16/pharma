package com.friendspharma.app.features.presentation.cart

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.friendspharma.app.R
import com.friendspharma.app.core.components.AppBar
import com.friendspharma.app.core.components.AppName
import com.friendspharma.app.core.components.ButtonK
import com.friendspharma.app.core.components.Loader
import com.friendspharma.app.core.theme.BackGroundDark
import com.friendspharma.app.core.theme.Gray
import com.friendspharma.app.core.theme.Primary
import com.friendspharma.app.core.theme.TextFieldBackGround
import com.friendspharma.app.core.util.KeyboardUnFocusHandler
import com.friendspharma.app.features.NavigationActions
import com.friendspharma.app.features.data.remote.model.ProductsDtoItem
import com.friendspharma.app.features.presentation.cart.components.AddressDialog
import com.friendspharma.app.features.presentation.cart.components.MinimumOrderDialog
import com.friendspharma.app.features.presentation.home.comonents.IncreaseDecrease

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navAction: NavigationActions,
    scrollSate: LazyListState = rememberLazyListState()
) {

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.cart),
                navAction = navAction,
                icon = R.drawable.baseline_shopping_cart_24
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->

        val state by viewModel.state.collectAsStateWithLifecycle()
        val context = LocalContext.current
        val openMinimumAmountDialog = remember { mutableStateOf(false) }

        KeyboardUnFocusHandler()

        if (state.showAddressDialog) {
            AddressDialog(
                addresses = state.addresses,
                onDismiss = { viewModel.closeAddressDialog() },
                onSelect = { viewModel.addressSelected(it) },
                insertAddress = { viewModel.insertAddress(it) },
                isLoading = state.addressLoading,
                changeAddress = { viewModel.changeAddress(it) },
                deleteAddress = { viewModel.deleteAddress(it) },
                selectedAddress = state.selectedAddress
            )
        }

        if (openMinimumAmountDialog.value) {
            MinimumOrderDialog {
                openMinimumAmountDialog.value = false
            }
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = Color.White)

        ) {

            LazyColumn(
                Modifier
                    .padding(horizontal = 10.dp)
                    .weight(1f),
                state = scrollSate
            ) {
                item { Spacer(modifier = Modifier.height(10.dp)) }
                items(state.cartInfoDto.data?.size ?: 0) {
                    val item = state.cartInfoDto.data?.get(it)

                    Card(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(TextFieldBackGround)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = item?.IMAGE_URL, contentDescription = null,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = item?.PRODUCT ?: "",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = item?.SALES_PRICE.toString() + "৳ * " + item?.QUANTITY + " ${item?.SALES_UNIT} " + " = " + item?.TOTAL_PRICE + "৳",
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    IncreaseDecrease(
                                        item = ProductsDtoItem(PID_PRODUCT = item?.PID_PRODUCT),
                                        removeFromCart = { product, salesUnit ->
                                            viewModel.removeFromCart(product, salesUnit, context)
                                        },
                                        increaseCartItem = { product, quantity, salesUnit ->
                                            viewModel.addToCart(
                                                product,
                                                quantity,
                                                salesUnit,
                                                context
                                            )
                                        },
                                        cartInfo = state.cartInfoDto,
                                        addToCartLoading = state.addToCartLoading
                                    )
                                }
                                Text("Save: ${((item?.OFFER_VALUE?.toInt() ?: 0) * (item?.QUANTITY?.toInt() ?: 0))} ৳ (${item?.SALES_PER}%)")
                            }
                        }
                    }
                }
                if (!state.cartInfoDto.data.isNullOrEmpty())
                    item {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .padding(top = 10.dp)
                        ) {
                            Text(
                                text = "Total Quantity: ${state.totalQuantity}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "Total Price: ${state.totalPrice}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                            if (!state.selectedAddress.ADDRESS.isNullOrEmpty())
                                Column {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row {
                                        Text(
                                            "Delivery address: ",
                                            fontSize = 16.sp,
                                            color = Gray,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(top = 5.dp)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    BackGroundDark.copy(0.75f),
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .weight(1f)
                                                .clickable {
                                                    viewModel.showAddressDialog()
                                                }
                                        ) {
                                            Text(
                                                (state.selectedAddress.ADDR_TYPE
                                                    ?: "") + ": " + state.selectedAddress.ADDRESS,
                                                fontSize = 16.sp, color = Gray,
                                                modifier = Modifier
                                                    .padding(5.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                }

                        }
                        Spacer(modifier = Modifier.height(20.dp))

                        ButtonK(text = R.string.submit_order) {
                            if (state.totalPrice < 500) {
                                openMinimumAmountDialog.value = true
                                return@ButtonK
                            }
                            viewModel.submitOrder(context, navAction::pop)
                        }
                    }

            }
            AppName()
        }


        if (state.isLoading)
            Loader(paddingValues = paddingValues)

    }
}
