package com.friendspharma.app.features.presentation.my_orders

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.friendspharma.app.R
import com.friendspharma.app.core.components.AppBar
import com.friendspharma.app.core.components.AppName
import com.friendspharma.app.core.components.Loader
import com.friendspharma.app.core.theme.BackGroundDark
import com.friendspharma.app.core.util.KeyboardUnFocusHandler
import com.friendspharma.app.features.NavigationActions
import kotlinx.coroutines.CoroutineScope

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MyOrdersScreen(
    viewModel: MyOrderViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navAction: NavigationActions,
    scope: CoroutineScope = rememberCoroutineScope(),
    scrollSate: LazyListState = rememberLazyListState()
) {

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.my_orders),
                navAction = navAction,
                icon = R.drawable.baseline_checklist_24
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->

        val state by viewModel.state.collectAsStateWithLifecycle()

        KeyboardUnFocusHandler()

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
                items(state.orders.data?.size ?: 0) {
                    val item = state.orders.data?.get(it)

                    Card(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .fillMaxWidth()
                            .clickable {
                                navAction.navToOrderDetails(item?.PID_TRAN_MST.toString())
                            },
                        elevation = CardDefaults.cardElevation(5.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(BackGroundDark, Color.White)
                                    )
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .padding(top = 10.dp, bottom = 2.dp)
                            ) {
                                Text(text = item?.ORDER_NO ?: "")
                                Spacer(modifier = Modifier.weight(1f))
                                Text(text = item?.ORDER_STATUS ?: "")
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .padding(bottom = 5.dp)
                            ) {
                                Text(
                                    text = item?.ORDER_DATE?.replace("T", "  ")?.replace("Z", "")
                                        ?: ""
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(text = item?.TOTAL_AMOUNT.toString() + "৳")
                            }
                        }
                    }
                }
            }
            AppName()
        }

        if (state.isLoading)
            Loader(paddingValues = paddingValues)

    }
}