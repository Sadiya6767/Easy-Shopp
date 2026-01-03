package com.example.easyshopp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easyshopp.pages.CategoryProductPage
import com.example.easyshopp.pages.CheckoutPage
import com.example.easyshopp.pages.OrderPage
import com.example.easyshopp.pages.ProductDetailsPage
import com.example.easyshopp.screen.AuthScreen
import com.example.easyshopp.screen.HomeScreen
import com.example.easyshopp.screen.LoginScreen
import com.example.easyshopp.screen.SignupScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    GlobalNavigation.navController = navController

    val isLoggedIn = Firebase.auth.currentUser != null
    val firstPage = if (isLoggedIn) "home" else "auth"

    NavHost(
        navController = navController,
        startDestination = firstPage
    ) {

        composable("auth") {
            AuthScreen(modifier, navController)
        }

        composable("login") {
            LoginScreen(modifier, navController)
        }

        composable("signup") {
            SignupScreen(modifier, navController)
        }

        composable("home") {
            HomeScreen(modifier, navController)
        }

        composable("category-product/{categoryId}") {
            var categoryId = it.arguments?.getString("categoryId")
            CategoryProductPage(modifier, categoryId?: "")
        }

        composable("product-details/{productId}") {
            var productId = it.arguments?.getString("productId")
            ProductDetailsPage(modifier, productId?: "")
        }

        composable("checkout") {
            CheckoutPage(modifier = Modifier)
        }

        composable("orders") {
            OrderPage(modifier = Modifier)
        }

    }
}
object GlobalNavigation{
    lateinit var navController : NavHostController
}
