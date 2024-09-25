package com.example.warehouse.storekeeper.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.warehouse.storekeeper.repositories.MainRepository
import com.example.warehouse.storekeeper.ui.screens.LoginScreen
import com.example.warehouse.storekeeper.ui.screens.OrdersScreen
import com.example.warehouse.storekeeper.ui.screens.ProductsScreen

private const val DESTINATION_LOGIN = "login"
private const val DESTINATION_ORDERS = "orders"
private const val DESTINATION_PRODUCTS = "products"

@Composable
fun AppNavigation(mainRepository: MainRepository) {
    val controller = rememberNavController()

    NavHost(
        navController = controller,
        startDestination = DESTINATION_LOGIN
    ) {
        composable(DESTINATION_LOGIN) {
            LoginScreen(
                mainRepository = mainRepository,
                onLogin = { controller.navigate(DESTINATION_ORDERS) }
            )
        }
        composable(DESTINATION_ORDERS) {
            OrdersScreen(
                mainRepository = mainRepository,
                onOrderSelected = { controller.navigate("$DESTINATION_PRODUCTS/$it") }
            )
        }
        composable(
            route = "$DESTINATION_PRODUCTS/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) {
            ProductsScreen(
                mainRepository = mainRepository,
                orderId = it.arguments?.getInt("orderId") ?: -1,
                onConfirmed = { controller.navigate(DESTINATION_ORDERS) }
            )
        }
    }
}