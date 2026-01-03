package com.example.easyshopp.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.easyshopp.pages.CartPage
import com.example.easyshopp.pages.FavoritePage
import com.example.easyshopp.pages.HomePage
import com.example.easyshopp.pages.ProfilePage

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {

    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Favorite", Icons.Default.Favorite),
        NavItem("Cart", Icons.Default.ShoppingCart),
        NavItem("Profile", Icons.Default.Person)
    )

    // FIXED STATE
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = index == selectedIndex,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = navItem.label
                            )
                        },
                        label = {
                            Text(navItem.label)
                        }
                    )
                }
            }
        }
    ) {
        ContentScreen(modifier = modifier.padding(it), selectedIndex)
    }
}


@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex : Int) {
    when(selectedIndex){
        0 -> HomePage(modifier)
        1 -> FavoritePage(modifier)
        2 -> CartPage(modifier)
        3 -> ProfilePage(modifier)
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector
)
