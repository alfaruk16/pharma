package com.friendspharma.app.features.presentation.home

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.friendspharma.app.R
import com.friendspharma.app.core.components.ActionItem
import com.friendspharma.app.core.components.AppBar
import com.friendspharma.app.core.components.AppName
import com.friendspharma.app.core.components.Drawer
import com.friendspharma.app.core.components.Loader
import com.friendspharma.app.core.components.TextFieldK
import com.friendspharma.app.core.theme.BackGroundDark
import com.friendspharma.app.core.theme.Primary
import com.friendspharma.app.core.theme.TextFieldBackGround
import com.friendspharma.app.features.NavigationActions
import com.friendspharma.app.features.data.remote.model.AllCategoryDtoItem
import com.friendspharma.app.features.data.remote.model.AllCompanyDtoItem
import com.friendspharma.app.features.data.remote.model.ProductsDtoItem
import com.friendspharma.app.features.presentation.home.comonents.AddCartDialogue
import com.friendspharma.app.features.presentation.home.comonents.BoxSwitch
import com.friendspharma.app.features.presentation.home.comonents.CartButton
import com.friendspharma.app.features.presentation.home.comonents.CategoryItem
import com.friendspharma.app.features.presentation.home.comonents.ExitDialogue
import com.friendspharma.app.features.presentation.home.comonents.ProductDetails
import com.friendspharma.app.features.presentation.home.comonents.ProductItem
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navAction: NavigationActions,
    scrollSate: LazyGridState = rememberLazyGridState(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    scope: CoroutineScope = rememberCoroutineScope()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val isExit = remember {
        mutableStateOf(false)
    }
    val searchFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current
    val cartItem = remember {
        mutableStateOf(ProductsDtoItem())
    }
    val width = LocalConfiguration.current.screenWidthDp.dp
    val productWidth = width / 2
    val productHeight = productWidth * 9 / 16
    val pagerState = rememberPagerState(initialPage = 0)
    val bannerHeight = (width * 9 / 20)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Drawer(
                    closeDrawer = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    navAction = navAction,
                    productByBox = {
                        viewModel.productsByBox(it)
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    sortByPharmaceutical = {
                        scope.launch {
                            drawerState.close()
                            viewModel.sortByPharmaceutical(scrollSate)
                        }
                    },
                    sortByCategory = {
                        scope.launch {
                            drawerState.close()
                            viewModel.sortByCategory(scrollSate)
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                AppBar(
                    title = stringResource(id = R.string.app_name),
                    navAction = navAction,
                    icon = R.drawable.logo_icon,
                    isBack = false,
                    actions = listOf(
                        ActionItem(
                            Icons.Filled.Person,
                            action = navAction::navToProfile
                        )
                    ),
                    suffix = {
                        CartButton(cartItemQuantity = state.cartItemQuantity) {
                            navAction.navToCart()
                        }
                    },
                    openDrawer = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) { paddingValues ->

            if (cartItem.value.PID_PRODUCT != null)
                AddCartDialogue(
                    item = cartItem.value,
                    cartInfo = state.cartInfo,
                    addToCart = { item, quantity, salesUnit ->
                        viewModel.addToCart(item, quantity, salesUnit, context)
                        cartItem.value = ProductsDtoItem()
                    },
                    onDismiss = {
                        cartItem.value = ProductsDtoItem()
                    })

            if (state.currentItem.PID_PRODUCT != null)
                ProductDetails(
                    item = state.currentItem,
                    cartIds = state.cartIds,
                    cartInfo = state.cartInfo,
                    addToCart = { product ->
                        cartItem.value = product
                    },
                    removeFromCart = { product, salesUnit ->
                        viewModel.removeFromCart(
                            product,
                            salesUnit,
                            context = context
                        )
                    },
                    increaseCartItem = { item, quantity, salesUnit ->
                        viewModel.addToCart(item, quantity, salesUnit, context)
                    }, addToCartLoading = state.addToCartLoading
                ) {
                    viewModel.updateCurrentItem(ProductsDtoItem())
                }

            if (isExit.value)
                ExitDialogue(activity = activity) {
                    isExit.value = false
                }

            BackHandler {
                if (!state.isSortedeByCategory) {
                    scope.launch {
                        viewModel.sortByCategory(scrollSate)
                    }
                } else {
                    isExit.value = true
                }
            }

            LaunchedEffect(key1 = Unit) {
                while (true) {
                    delay(5000)
                    with(pagerState) {
                        val target = if (currentPage < pageCount - 1) currentPage + 1 else 0
                        tween<Float>(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                        animateScrollToPage(page = target)
                    }
                }

            }

            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        viewModel.init()
                    }
                }

                lifecycleOwner.lifecycle.addObserver(observer)

                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextFieldK(
                        value = state.search,
                        onValueChange = { viewModel.searchChanged(it, focusManager) },
                        focusRequester = searchFocusRequester,
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = null,
                                tint = Primary
                            )
                        },
                        label = R.string.search,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .weight(1f),
                        height = 40.dp,
                        containerColor = TextFieldBackGround,
                        cornerRadius = 20,
                        borderColor = Color.Transparent,
                        placeHolderFontSize = 14
                    )

                    BoxSwitch(isBox = state.isBox) {
                        viewModel.switch()
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }

                LazyVerticalGrid(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    state = scrollSate,
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(5.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        HorizontalPager(
                            count = state.companies.data?.size ?: 0, state = pagerState
                        ) {
                            val item = remember {
                                state.companies.data?.get(it)
                            }
                            AsyncImage(
                                model = item?.IMAGE_BANNER_URL ?: "",
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .width(width)
                                    .height(bannerHeight)
                                    .clip(shape = RoundedCornerShape(15.dp))
                                    .clickable {
                                        navAction.navToPharmaMedicine(
                                            item ?: AllCompanyDtoItem()
                                        )
                                    }
                            )
                        }
                    }

                    item(span = { GridItemSpan(2) }) {
                        LazyRow(contentPadding = PaddingValues(5.dp)) {
                            items(state.categories.data?.size ?: 0) {
                                val item = state.categories.data?.get(it)
                                CategoryItem(
                                    item ?: AllCategoryDtoItem(),
                                    isSelected = state.currentCategoryIndex == it
                                ) {
                                    scope.launch {
                                        viewModel.categorySelected(it)
                                    }
                                }
                            }
                        }
                    }

                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    if (state.currentCategoryIndex != -1) {
                        item(span = { GridItemSpan(2) }) {
                            Box(modifier = Modifier.padding(5.dp)) {
                                Box(
                                    modifier = Modifier
                                        .border(
                                            BorderStroke(1.dp, Primary),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .fillMaxWidth()
                                        .padding(horizontal = 5.dp)
                                ) {
                                    Text(
                                        text = state.allSearchedProduct[state.currentCategoryIndex].category
                                            ?: "",
                                        fontWeight = FontWeight.W500,
                                        color = Primary,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(5.dp)
                                    )
                                }
                            }
                        }

                        items(
                            state.allSearchedProduct[state.currentCategoryIndex].data?.size ?: 0
                        ) {
                            ProductItem(
                                state.allSearchedProduct[state.currentCategoryIndex].data?.get(it)
                                    ?: ProductsDtoItem(),
                                cartIds = state.cartIds,
                                cartInfo = state.cartInfo,
                                onTap = { product ->
                                    viewModel.updateCurrentItem(product)
                                },
                                addToCart = { product ->
                                    cartItem.value = product
                                },
                                removeFromCart = { product, salesUnit ->
                                    viewModel.removeFromCart(product, salesUnit, context = context)
                                },
                                increaseCartItem = { item, quantity, salesUnit ->
                                    viewModel.addToCart(item, quantity, salesUnit, context)
                                },
                                height = productHeight,
                                width = productWidth,
                                isBox = state.isBox,
                                addToCartLoading = state.addToCartLoading
                            )
                        }
                    }

                    state.allSearchedProduct.forEach { category ->

                        if (state.currentCategoryIndex == -1 || category.category != state.allSearchedProduct[state.currentCategoryIndex].category)
                            item(span = { GridItemSpan(2) }) {
                                Box(modifier = Modifier.padding(5.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .border(
                                                BorderStroke(1.dp, Primary),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .fillMaxWidth()
                                            .padding(horizontal = 5.dp)
                                    ) {
                                        Text(
                                            text = category.category ?: "",
                                            fontWeight = FontWeight.W500,
                                            color = Primary,
                                            fontSize = 18.sp,
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }
                                }
                            }
                        if (state.currentCategoryIndex == -1 || category.category != state.allSearchedProduct[state.currentCategoryIndex].category)
                            items(category.data?.size ?: 0) {
                                ProductItem(
                                    category.data?.get(it) ?: ProductsDtoItem(),
                                    cartIds = state.cartIds,
                                    cartInfo = state.cartInfo,
                                    onTap = { product ->
                                        viewModel.updateCurrentItem(product)
                                    },
                                    addToCart = { product ->
                                        cartItem.value = product
                                    },
                                    removeFromCart = { product, salesUnit ->
                                        viewModel.removeFromCart(
                                            product,
                                            salesUnit,
                                            context = context
                                        )
                                    },
                                    increaseCartItem = { item, quantity, salesUnit ->
                                        viewModel.addToCart(item, quantity, salesUnit, context)
                                    }, height = productHeight, width = productWidth, state.isBox,
                                    addToCartLoading = state.addToCartLoading
                                )
                            }
                    }
                }
                AppName()
            }

            if (state.isLoading)
                Loader(paddingValues = paddingValues)

        }
    }
}