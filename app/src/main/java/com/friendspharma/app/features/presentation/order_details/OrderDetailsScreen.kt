package com.friendspharma.app.features.presentation.order_details

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
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
import com.friendspharma.app.core.theme.BackGroundColor
import com.friendspharma.app.core.util.KeyboardUnFocusHandler
import com.friendspharma.app.features.NavigationActions
import com.friendspharma.app.features.data.remote.model.OrderDetailsDtoItem
import com.friendspharma.app.features.presentation.order_details.components.CourierOptionsDialog

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun OrderDetailsScreen(
    viewModel: OrderDetailsViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navAction: NavigationActions,
    scrollSate: LazyListState = rememberLazyListState()
) {

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.order_details),
                navAction = navAction,
                icon = R.drawable.baseline_checklist_24
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->

        val state by viewModel.state.collectAsStateWithLifecycle()
        val openCourierOptionDialog = remember { mutableStateOf(false) }
        val context = LocalContext.current

        if (openCourierOptionDialog.value) {
            CourierOptionsDialog(title = stringResource(R.string.select_courier),
                onDismiss = { openCourierOptionDialog.value = false },
                onSelected = {
                    openCourierOptionDialog.value = false
                    if (it == "Pathao") {
                        navAction.navigateToPathaoCourier(
                            state.orders.data?.get(0) ?: OrderDetailsDtoItem()
                        )
                    } else {
                        navAction.navToSteadFastCourier(
                            state.orders.data?.get(0) ?: OrderDetailsDtoItem()
                        )
                    }
                }
            )
        }

        KeyboardUnFocusHandler()

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = BackGroundColor)

        ) {

            LazyColumn(
                Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f),
                state = scrollSate
            ) {
                item { Spacer(modifier = Modifier.height(20.dp)) }
                if (!state.orders.data.isNullOrEmpty())
                    item {
                        InfoItem("Order no: ", state.orders.data?.get(0)?.ORDER_NO ?: "")
                    }
                if (!state.orders.data.isNullOrEmpty())
                    item {
                        InfoItem(
                            "Date: ",
                            state.orders.data?.get(0)?.ORDER_DATE?.replace("T", "  ")
                                ?.replace("Z", "")
                                ?: ""
                        )
                    }
                if (!state.orders.data.isNullOrEmpty())
                    item {
                        InfoItem("Order to: ", state.orders.data?.get(0)?.DELIVERY_ADDRESS ?: "")
                    }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                repeat(state.orders.data?.size ?: 0) {
                    val item = state.orders.data?.get(it)
                    item {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = item?.IMAGE_URL, contentDescription = null,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                                    Text(
                                        text = item?.PRODUCT ?: "",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W600
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = item?.SALES_PRICE.toString() + "৳ * " + item?.QUANTITY + " ${item?.SALES_UNIT} " + " = " + item?.TOTAL_PRICE + "৳")
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                    Text("Save: ${((item?.OFFER_VALUE?.toInt() ?: 0) * (item?.QUANTITY ?: 0))} ৳ (${item?.SALES_PER}%)")
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
                if (!state.orders.data.isNullOrEmpty())
                    item {
                        Card(
                            colors = CardDefaults.cardColors(Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)

                            ) {
                                InfoItem("Total Amount: ", state.totalAmount.toString())
                                InfoItem("Discount: (-) ", state.discount.toString())
                                InfoItem("Grand Total: ", state.totalPrice.toString())
                            }

                        }
                    }
                item {
                    Spacer(Modifier.height(20.dp))
                }
                if (!state.orders.data.isNullOrEmpty())
                    item {
                        ButtonK(R.string.courier) {
                            openCourierOptionDialog.value = true
                        }
                    }
            }
            AppName()
        }


        if (state.isLoading)
            Loader(paddingValues = paddingValues)

    }
}

@Composable
fun InfoItem(title: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 10.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = value,
            fontWeight = FontWeight.Bold
        )
    }
}