package com.friendspharma.app.features.presentation.pharma

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.friendspharma.app.R
import com.friendspharma.app.core.components.ActionItem
import com.friendspharma.app.core.components.AppBar
import com.friendspharma.app.core.components.AppName
import com.friendspharma.app.core.components.Loader
import com.friendspharma.app.core.components.TextFieldK
import com.friendspharma.app.core.theme.Primary
import com.friendspharma.app.core.theme.TextFieldBackGround
import com.friendspharma.app.features.MainNavigation
import com.friendspharma.app.features.NavigationActions
import com.friendspharma.app.features.data.remote.model.AllCompanyDtoItem
import com.friendspharma.app.features.presentation.home.comonents.CartButton
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PharmaScreen(
    viewModel: PharmaViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navAction: NavigationActions,
    mainNavAction: MainNavigation,
    scrollSate: LazyGridState = rememberLazyGridState(),
    scope: CoroutineScope = rememberCoroutineScope()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current
    val width = LocalConfiguration.current.screenWidthDp.dp
    val productWidth = width / 2
    val productHeight = productWidth * 9 / 16
    val pagerState = rememberPagerState(initialPage = 0)
    val bannerHeight = (width * 9 / 20)

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.pharma),
                navAction = navAction,
                icon = R.drawable.logo_icon,
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
                onBackPressed = {
                    mainNavAction.pop()
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->

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
                    label = R.string.search_for_company,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .weight(1f),
                    height = 40.dp,
                    containerColor = TextFieldBackGround,
                    cornerRadius = 20,
                    borderColor = Color.Transparent,
                    placeHolderFontSize = 14
                )

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
                        count =  state.companies.data?.size ?: 0 , state = pagerState
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
                                .clip(shape = RoundedCornerShape(15.dp)).clickable {
                                    navAction.navToPharmaMedicine(item ?: AllCompanyDtoItem())
                                }
                        )
                    }
                }

                items(state.allSearchedCompanies.data?.size ?: 0) {
                    val item = state.allSearchedCompanies.data?.get(it)
                    Card(
                        colors = CardDefaults.cardColors(Color.White),
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 5.dp)
                            .clickable {
                                scope.launch {
                                    navAction.navToPharmaMedicine(item ?: AllCompanyDtoItem())
                                }
                            }
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(5.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = item?.IMAGE_LOGO_URL ?: "", contentDescription = null,
                                modifier = Modifier
                                    .height(productHeight)
                                    .padding(10.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = item?.COMPANY_NAME ?: "", color = Primary,
                                fontSize = 14.sp, fontWeight = FontWeight.W500,
                                maxLines = 1,
                                modifier = Modifier.padding(5.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }
                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(10.dp))
                }

            }
            AppName()
        }

        if (state.isLoading)
            Loader(paddingValues = paddingValues)

    }
}