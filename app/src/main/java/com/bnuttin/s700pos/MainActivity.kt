package com.bnuttin.s700pos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bnuttin.s700pos.models.CartViewModel
import com.bnuttin.s700pos.models.CheckoutViewModel
import com.bnuttin.s700pos.models.CustomerViewModel
import com.bnuttin.s700pos.models.ProductViewModel
import com.bnuttin.s700pos.models.SettingsViewModel
import com.bnuttin.s700pos.pages.Checkout
import com.bnuttin.s700pos.pages.Customer
import com.bnuttin.s700pos.pages.Settings
import com.bnuttin.s700pos.pages.Shop
import com.bnuttin.s700pos.theme.S700POSTheme
import com.example.s700pos.R

class MainActivity : ComponentActivity() {
//    if (ContextCompat.checkSelfPermission(getActivity(),
//    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
//        // REQUEST_CODE_LOCATION should be defined on your app level
//        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE_LOCATION);
//    }
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
    val navController = rememberNavController()

    val cartViewModel: CartViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val customerViewModel: CustomerViewModel = viewModel()
    val checkoutViewModel: CheckoutViewModel = viewModel()
    var settingsViewModel: SettingsViewModel = viewModel()

//    if (ContextCompat.checkSelfPermission(context,
//            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        // REQUEST_CODE_LOCATION should be defined on your app level
//        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_LOCATION)
//    }

//    val listener = object: TerminalListener{
//        override fun onUnexpectedReaderDisconnect(reader: Reader) {
//            Log.d("BENJI", "reader disconnected")
//        }
//    }
//    val logLevel = LogLevel.VERBOSE
//    val tokenProvider = TokenProvider()
//    if (!Terminal.isInitialized()) {
//        Terminal.initTerminal(context, logLevel, tokenProvider, listener)
//    }
//    Terminal.getInstance()

    S700POSTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        IconButton(onClick = { navController.navigate("shop") }) {
                            Icon(
                                painterResource(R.drawable.outline_home_24),
                                contentDescription = "Shop"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.LightGray,
                        titleContentColor = Color.DarkGray
                    ),
                    actions = {
                        IconButton(onClick = { navController.navigate("shop") }) {
                            Icon(
                                painterResource(R.drawable.baseline_grid_view_24),
                                contentDescription = "Shop",
                            )
                        }
                        IconButton(onClick = { navController.navigate("checkout") }) {
                            BadgedBox(badge = {
                                if (cartViewModel.items.isNotEmpty()) {
                                    Badge {
                                        Text(cartViewModel.items.size.toString())
                                    }
                                }
                            }) {
                                Icon(
                                    painterResource(R.drawable.shopping_cart),
                                    contentDescription = "Shop"
                                )
                            }
                        }
                        IconButton(onClick = { navController.navigate("customer") }) {
                            BadgedBox(badge = {
                                if (customerViewModel.customer.id !== null) {
                                    Badge(
                                        containerColor = Green
                                    ) {
                                        Text("")
                                    }
                                }
                            }) {
                                Icon(
                                    painterResource(R.drawable.person),
                                    contentDescription = "Customer",
                                )
                            }
                        }
                        IconButton(onClick = { navController.navigate("settings") }) {
                            Icon(
                                painterResource(R.drawable.settings),
                                contentDescription = "Shop",
                            )
                        }
                    }
                )
            }
        ) {
            NavHost(navController = navController, startDestination = "shop") {
                composable("shop") {
                    Shop(productViewModel, cartViewModel)
                }
                composable("checkout") {
                    Checkout(cartViewModel, checkoutViewModel)
                }
                composable("customer") {
                    Customer(customerViewModel)
                }
                composable("settings") {
                    Settings(settingsViewModel)
                }
            }
        }
    }

}
