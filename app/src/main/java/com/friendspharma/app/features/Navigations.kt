package com.friendspharma.app.features

import androidx.annotation.StringRes
import androidx.navigation.NavController
import com.friendspharma.app.R
import com.friendspharma.app.features.Screens.CART_SCREEN
import com.friendspharma.app.features.Screens.CATEGORIES_SCREEN
import com.friendspharma.app.features.Screens.CATEGORY_MEDICINE_SCREEN
import com.friendspharma.app.features.Screens.HOME_SCREEN
import com.friendspharma.app.features.Screens.LOGIN_SCREEN
import com.friendspharma.app.features.Screens.MAIN_SCREEN
import com.friendspharma.app.features.Screens.MY_ORDERS_SCREEN
import com.friendspharma.app.features.Screens.ORDER_DETAILS_SCREEN
import com.friendspharma.app.features.Screens.PATHAO_COURIER_SCREEN
import com.friendspharma.app.features.Screens.PHARMAA_MEDICINE_SCREEN
import com.friendspharma.app.features.Screens.PHARMA_SCREEN
import com.friendspharma.app.features.Screens.PROFILE_SCREEN
import com.friendspharma.app.features.Screens.RESET_PASSWORD_SCREEN
import com.friendspharma.app.features.Screens.SIGN_UP_SCREEN
import com.friendspharma.app.features.Screens.SPLASH_SCREEN
import com.friendspharma.app.features.Screens.STEAD_FAST_COURIER_SCREEN
import com.friendspharma.app.features.data.remote.model.AllCategoryDtoItem
import com.friendspharma.app.features.data.remote.model.AllCompanyDtoItem
import com.friendspharma.app.features.data.remote.model.OrderDetailsDtoItem
import com.google.gson.Gson

object Screens {
    const val SPLASH_SCREEN = "splashScreen"
    const val LOGIN_SCREEN = "loginScreen"
    const val SIGN_UP_SCREEN = "signUpScreen"
    const val HOME_SCREEN = "homeScreen"
    const val RESET_PASSWORD_SCREEN = "resetPasswordScreen"
    const val PROFILE_SCREEN = "profileScreen"
    const val CART_SCREEN = "cartScreen"
    const val MY_ORDERS_SCREEN = "myOrdersScreen"
    const val ORDER_DETAILS_SCREEN = "orderDetailsScreen"
    const val PATHAO_COURIER_SCREEN = "pathoaCourierScreen"
    const val STEAD_FAST_COURIER_SCREEN = "steadFastCourierScreen"
    const val PHARMA_SCREEN = "pharmaScreen"
    const val MAIN_SCREEN = "mainScreen"
    const val PHARMAA_MEDICINE_SCREEN = "pharmaMedicineScreen"
    const val CATEGORY_MEDICINE_SCREEN = "categoryMedicineScreen"
    const val CATEGORIES_SCREEN = "categoriesScreen"
}

object ScreenArgs {
    const val DATA = "data"
}

sealed class ScreenRoute(
    val route: String,
    @StringRes val title: Int? = null,
    val icon: Int? = null,
) {
    data object Splash : ScreenRoute(SPLASH_SCREEN)
    data object Login : ScreenRoute(LOGIN_SCREEN)
    data object SignUp : ScreenRoute(SIGN_UP_SCREEN)
    data object Main : ScreenRoute(MAIN_SCREEN)
    data object Home : ScreenRoute(HOME_SCREEN, R.string.home, icon = R.drawable.baseline_home_24)
    data object ResetPassword : ScreenRoute(RESET_PASSWORD_SCREEN)
    data object Profile : ScreenRoute(PROFILE_SCREEN)
    data object Cart : ScreenRoute(CART_SCREEN)
    data object MyOrders : ScreenRoute(MY_ORDERS_SCREEN)
    data object OrderDetails : ScreenRoute("${ORDER_DETAILS_SCREEN}/{${ScreenArgs.DATA}}")
    data object PathaoCourier :
        ScreenRoute("${PATHAO_COURIER_SCREEN}?${ScreenArgs.DATA}={${ScreenArgs.DATA}}")

    data object SteadFastCourier :
        ScreenRoute("${STEAD_FAST_COURIER_SCREEN}?${ScreenArgs.DATA}={${ScreenArgs.DATA}}")

    data object Pharma :
        ScreenRoute(PHARMA_SCREEN, R.string.pharma, R.drawable.baseline_warehouse_24)
    data object Categories : ScreenRoute(CATEGORIES_SCREEN, R.string.categories, R.drawable.baseline_category_24)


    data object PharmaMedicine : ScreenRoute("${PHARMAA_MEDICINE_SCREEN}?${ScreenArgs.DATA}={${ScreenArgs.DATA}}")
    data object CategoryMedicine : ScreenRoute("${CATEGORY_MEDICINE_SCREEN}?${ScreenArgs.DATA}={${ScreenArgs.DATA}}")

}

class MainNavigation(private val navController: NavController) {
    fun pop() {
        navController.navigateUp()
    }
}

class NavigationActions(private val navController: NavController) {

    fun pop() {
        navController.navigateUp()
    }

    fun navToCategoryMedicine(category: AllCategoryDtoItem) {
        navController.navigate("${CATEGORY_MEDICINE_SCREEN}?${ScreenArgs.DATA}=${
            Gson().toJson(category)
        }") {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navToPharmaMedicine(company: AllCompanyDtoItem) {
        navController.navigate("${PHARMAA_MEDICINE_SCREEN}?${ScreenArgs.DATA}=${
            Gson().toJson(company)
        }") {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navToCart() {
        navController.navigate(CART_SCREEN) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navToSignUp() {
        navController.navigate(SIGN_UP_SCREEN) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navToMain() {
        navController.navigate(MAIN_SCREEN) {
            launchSingleTop = true
            restoreState = true
            popUpTo(0)
        }
    }

    fun navToResetPassword() {
        navController.navigate(RESET_PASSWORD_SCREEN) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navToProfile() {
        navController.navigate(PROFILE_SCREEN) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navToLogin() {
        navController.navigate(LOGIN_SCREEN) {
            launchSingleTop = true
            restoreState = true
            popUpTo(0)
        }
    }

    fun navToMyOrders() {
        navController.navigate(MY_ORDERS_SCREEN) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navToOrderDetails(id: String) {
        navController.navigate("${ORDER_DETAILS_SCREEN}/${id}") {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToPathaoCourier(order: OrderDetailsDtoItem) {
        navController.navigate(
            "${PATHAO_COURIER_SCREEN}?${ScreenArgs.DATA}=${
                Gson().toJson(
                    order
                )
            }"
        ) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navToSteadFastCourier(order: OrderDetailsDtoItem) {
        navController.navigate(
            "${STEAD_FAST_COURIER_SCREEN}?${ScreenArgs.DATA}=${
                Gson().toJson(
                    order
                )
            }"
        ) {
            launchSingleTop = true
            restoreState = true
        }
    }

}