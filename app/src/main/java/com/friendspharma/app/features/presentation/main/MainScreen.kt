package com.friendspharma.app.features.presentation.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.friendspharma.app.core.theme.Gray
import com.friendspharma.app.core.theme.Primary
import com.friendspharma.app.features.MainNavigation
import com.friendspharma.app.features.NavigationActions
import com.friendspharma.app.features.ScreenRoute
import com.friendspharma.app.features.presentation.categories.CategoriesScreen
import com.friendspharma.app.features.presentation.home.HomeScreen
import com.friendspharma.app.features.presentation.pharma.PharmaScreen

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    navItems: List<ScreenRoute> = listOf(
        ScreenRoute.Home,
        ScreenRoute.Categories,
        ScreenRoute.Pharma
    ),
    navAction: NavigationActions,
    mainNavAction: MainNavigation = remember(navController) {
        MainNavigation(navController)
    }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val config = LocalConfiguration.current

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                modifier = Modifier
                    .height(64.dp)
            ) {

                navItems.onEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Primary,
                            unselectedIconColor = Gray
                        ),
                        icon = {
                            Image(
                                painter = painterResource(id = screen.icon ?: 0),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(id = screen.title ?: 0),
                                textAlign = TextAlign.Center
                            )
                        },
                        onClick = {
                            if (screen.route != currentDestination?.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.Home.route,
            Modifier.padding(paddingValues)
        ) {
            composable(ScreenRoute.Home.route) {
                HomeScreen(navAction = navAction)
            }

            composable(ScreenRoute.Categories.route) {
                CategoriesScreen(navAction = navAction, mainNavAction = mainNavAction)
            }

            composable(ScreenRoute.Pharma.route) {
                PharmaScreen(navAction = navAction, mainNavAction = mainNavAction)
            }
        }
    }

}
