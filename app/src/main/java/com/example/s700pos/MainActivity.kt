package com.example.s700pos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.s700pos.ui.screens.Checkout
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
    var mDisplayMenu by remember { mutableStateOf(false) }
    val navController = rememberNavController()

    S700POSTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "POS App",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description"
                            )
                        }

                        DropdownMenu(
                            expanded = mDisplayMenu,
                            onDismissRequest = { mDisplayMenu = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    navController.navigate("shop")
                                    mDisplayMenu = false
                                },
                                text = {
                                    Row {
                                        Icon(
                                            painterResource(R.drawable.shopping_cart),
                                            contentDescription = "Shop",
                                            modifier = Modifier.padding(end=10.dp)
                                        )
                                        Text("Shop")
                                    }
                                }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    navController.navigate("checkout")
                                    mDisplayMenu = false
                                },
                                text = {
                                    Row {
                                        Icon(
                                            painterResource(R.drawable.credit_card),
                                            contentDescription = "Checkout",
                                            modifier = Modifier.padding(end=10.dp)
                                        )
                                        Text("Checkout")
                                    }
                                }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    navController.navigate("settings")
                                    mDisplayMenu = false
                                },
                                text = {
                                    Row {
                                        Icon(
                                            painterResource(R.drawable.settings),
                                            contentDescription = "Checkout",
                                            modifier = Modifier.padding(end=10.dp)
                                        )
                                        Text("Settings")
                                    }
                                }
                            )
                        }
                    }
                )
            }
        ) {
            NavHost(navController = navController, startDestination = "shop") {
                composable("shop") {
                    Shop()
                }
                composable("checkout") {
                    Checkout()
                }
                composable("settings") {
                    Settings()
                }
            }
        }
    }

}
