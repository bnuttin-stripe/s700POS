package com.example.s700pos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.s700pos.ui.models.CartViewModel
import com.example.s700pos.ui.models.CustomerViewModel
import com.example.s700pos.ui.models.ProductViewModel
import com.example.s700pos.ui.screens.Checkout
import com.example.s700pos.ui.screens.Customer
import com.example.s700pos.ui.screens.PreferencesManager
import com.example.s700pos.ui.screens.Settings
import com.example.s700pos.ui.screens.Shop
import com.example.s700pos.ui.theme.S700POSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            mainContent()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mainContent() {
    var displayMenu by remember { mutableStateOf(false) }
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val customerViewModel: CustomerViewModel = viewModel()

    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val shopName = remember { mutableStateOf(preferencesManager.getData("shopName", "POS App")) }
    val backendUrl = remember { mutableStateOf(preferencesManager.getData("backendUrl", "")) }

    S700POSTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        IconButton(onClick = { navController.navigate("shop") }) {
                            Icon(
                                painterResource(R.drawable.outline_home_24),
                                contentDescription = "Shop",
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.LightGray, titleContentColor = Color.DarkGray),
                    actions = {
                        IconButton(onClick = { navController.navigate("shop") }) {
                            Icon(
                                painterResource(R.drawable.shopping_cart),
                                contentDescription = "Shop",
                            )
                        }
                        IconButton(onClick = { navController.navigate("checkout") }) {
                            Icon(
                                painterResource(R.drawable.credit_card),
                                contentDescription = "Shop",
                            )
                        }
                        IconButton(onClick = { navController.navigate("customer") }) {
                            Icon(
                                painterResource(R.drawable.person),
                                contentDescription = "Customer",
                            )
                        }
                        IconButton(onClick = { navController.navigate("settings") }) {
                            Icon(
                                painterResource(R.drawable.settings),
                                contentDescription = "Shop",
                            )
                        }
//                        IconButton(onClick = { displayMenu = !displayMenu }) {
//                            Icon(
//                                imageVector = Icons.Filled.Menu,
//                                contentDescription = "Localized description"
//                            )
//                        }

//                        DropdownMenu(
//                            expanded = displayMenu,
//                            onDismissRequest = { displayMenu = false }
//                        ) {
//                            DropdownMenuItem(
//                                onClick = {
//                                    navController.navigate("shop")
//                                    displayMenu = false
//                                },
//                                text = {
//                                    Row {
//                                        Icon(
//                                            painterResource(R.drawable.shopping_cart),
//                                            contentDescription = "Shop",
//                                            modifier = Modifier.padding(end=10.dp)
//                                        )
//                                        Text("Shop")
//                                    }
//                                }
//                            )
//                            DropdownMenuItem(
//                                onClick = {
//                                    navController.navigate("checkout")
//                                    displayMenu = false
//                                },
//                                text = {
//                                    Row {
//                                        Icon(
//                                            painterResource(R.drawable.credit_card),
//                                            contentDescription = "Checkout",
//                                            modifier = Modifier.padding(end=10.dp)
//                                        )
//                                        Text("Checkout")
//                                    }
//                                }
//                            )
//                            DropdownMenuItem(
//                                onClick = {
//                                    navController.navigate("settings")
//                                    displayMenu = false
//                                },
//                                text = {
//                                    Row {
//                                        Icon(
//                                            painterResource(R.drawable.settings),
//                                            contentDescription = "Checkout",
//                                            modifier = Modifier.padding(end=10.dp)
//                                        )
//                                        Text("Settings")
//                                    }
//                                }
//                            )
//                        }
                    }
                )
            }
        ) {
            NavHost(navController = navController, startDestination = "shop") {
                composable("shop") {
                    Shop(productViewModel, cartViewModel, navController)
                    Text("Welcome to " + shopName.value)
                }
                composable("checkout") {
                    Checkout(cartViewModel)
                }
                composable("customer") {
                    Customer(customerViewModel)
                }
                composable("settings") {
                    Settings()
                }
            }
        }
    }

}
