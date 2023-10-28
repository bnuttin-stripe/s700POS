package com.bnuttin.s700pos

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bnuttin.s700pos.api.SettingsViewModel
import com.bnuttin.s700pos.api.TokenProvider
import com.bnuttin.s700pos.api.discoveryCancelable
import com.bnuttin.s700pos.pages.Checkout
import com.bnuttin.s700pos.pages.Customer
import com.bnuttin.s700pos.pages.CustomerList
import com.bnuttin.s700pos.pages.Payments
import com.bnuttin.s700pos.pages.QRSCanner
import com.bnuttin.s700pos.pages.Settings
import com.bnuttin.s700pos.pages.Shop
import com.bnuttin.s700pos.theme.S700POSTheme
import com.bnuttin.s700pos.viewmodels.CartViewModel
import com.bnuttin.s700pos.viewmodels.CheckoutViewModel
import com.bnuttin.s700pos.viewmodels.CustomerViewModel
import com.bnuttin.s700pos.viewmodels.ProductViewModel
import com.example.s700pos.R
import com.stripe.stripeterminal.Terminal
import com.stripe.stripeterminal.external.callable.Callback
import com.stripe.stripeterminal.external.callable.Cancelable
import com.stripe.stripeterminal.external.callable.DiscoveryListener
import com.stripe.stripeterminal.external.callable.ReaderCallback
import com.stripe.stripeterminal.external.callable.TerminalListener
import com.stripe.stripeterminal.external.models.ConnectionConfiguration
import com.stripe.stripeterminal.external.models.DiscoveryConfiguration
import com.stripe.stripeterminal.external.models.Reader
import com.stripe.stripeterminal.external.models.TerminalException
import com.stripe.stripeterminal.log.LogLevel

@SuppressLint("MissingPermission")
class MainActivity : ComponentActivity() {
    private fun onRequestPermissions() {
        if (Build.VERSION.SDK_INT >= 31) {
            val requiredPermissions = mutableListOf<String>().apply {
                add(Manifest.permission.ACCESS_FINE_LOCATION)
                add(Manifest.permission.BLUETOOTH_CONNECT)
                add(Manifest.permission.BLUETOOTH_SCAN)
            }.toTypedArray()
            locationPermissionRequest.launch(requiredPermissions)
        } else {
            locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    private fun onPermissionResult(result: Map<String, Boolean>) {
        val rejectedPermissions: List<String> = result
            .filter { !it.value }
            .map { it.key }

        if (rejectedPermissions.isEmpty()) onPermissionsGranted()
        else onPermissionsRejected(rejectedPermissions)
    }

    /**
     * Invoked when the user rejects any of the required permissions prompts.
     */
    private fun onPermissionsRejected(rejectedPermissions: List<String>) {
        //Timber.w("Permissions Rejected: ${rejectedPermissions.joinToString()}")
        Log.d("BENJI", "Permissions rejected")
    }

    /**
     * Invoked when the user accepts all of the required permissions prompts.
     */
    private fun onPermissionsGranted() {
//        navController.navigate(
//            TestJigScreen.SelectMerchant,
//            navOptions {
//                popUpTo(TestJigScreen.Permissions.name) { inclusive = true }
//            }
//        )
        Log.d("BENJI", "Permissions granted")
    }

    private val locationPermissionRequest: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            ::onPermissionResult
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onRequestPermissions()
        initTerminal()
        onDiscoverReaders()
        setContent {
            mainContent()
        }
    }

    fun initTerminal() {
        Log.d("BENJI", "Launching initTerminal")
        // Create your listener object. Override any methods that you want to be notified about
        val listener = object : TerminalListener {
            override fun onUnexpectedReaderDisconnect(reader: Reader) {
                Log.d("BENJI", "Unexpected disconnect")
            }
        }

        // Choose the level of messages that should be logged to your console
        val logLevel = LogLevel.VERBOSE

        // Create your token provider.
        val tokenProvider = TokenProvider()

        // Pass in the current application context, your desired logging level, your token provider, and the listener you created
        if (!Terminal.isInitialized()) {
            Log.d("BENJI", "Initializing Terminal")
            Terminal.initTerminal(applicationContext, logLevel, tokenProvider, listener)
        }

        // Since the Terminal is a singleton, you can call getInstance whenever you need it
        Terminal.getInstance()
    }

    fun connectReader(reader: Reader) {
        val config = ConnectionConfiguration.LocalMobileConnectionConfiguration("{{LOCATION_ID}}")
        Terminal.getInstance().connectLocalMobileReader(reader, config, object : ReaderCallback {
            override fun onSuccess(r: Reader) {
                Log.d("BENJI", "Connected to reader " + r.id)
            }

            override fun onFailure(e: TerminalException) {
                e.printStackTrace()
            }
        })
    }

    fun onDiscoverReaders() {
        var discoveryCancelable: Cancelable? = null
        val isApplicationDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE

        val config = DiscoveryConfiguration.LocalMobileDiscoveryConfiguration(
            isSimulated = isApplicationDebuggable,
        )
        // Save this cancelable to an instance variable
        discoveryCancelable = Terminal.getInstance().discoverReaders(config,
            object : DiscoveryListener {
                override fun onUpdateDiscoveredReaders(readers: List<Reader>) {
                    Log.d("BENJI", "onUpdateDiscoveredReaders: ${readers.size}")
                    //Will get list of readers. Then pick one and call  Terminal.getInstance().connectLocalMobileReader(
                    connectReader(readers.first())
                }
            },
            object : Callback {
                override fun onSuccess() {
                    println("Finished discovering readers")
                }

                override fun onFailure(e: TerminalException) {
                    e.printStackTrace()
                }
            })
    }

    override fun onStop() {
        super.onStop()
        // If you're leaving the activity or fragment without selecting a reader,
        // make sure you cancel the discovery process or the SDK will be stuck in
        // a discover readers phase
        discoveryCancelable?.cancel(object : Callback {
            override fun onSuccess() {}
            override fun onFailure(e: TerminalException) {}
        })
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
    val settingsViewModel: SettingsViewModel = viewModel()

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
                        IconButton(onClick = { navController.navigate("customers") }) {
//                            BadgedBox(badge = {
//                                if (customerViewModel.customer.id !== null) {
//                                    Badge(
//                                        containerColor = Green
//                                    ) {
//                                        Text("")
//                                    }
//                                }
//                            }) {
                                Icon(
                                    painterResource(R.drawable.person),
                                    contentDescription = "Customer",
                                )
//                            }
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
            NavHost(
                navController = navController,
                startDestination = "shop",
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() }
            ) {
                composable("shop") {
                    Shop(productViewModel, cartViewModel)
                }
                composable("checkout") {
                    Checkout(cartViewModel, checkoutViewModel)
                }
                composable("customers") {
                    CustomerList(customerViewModel, navController)
                }
                composable("customer/{customerId}") { navBackStackEntry ->
                    val customerId = navBackStackEntry.arguments?.getString("customerId")
                    /* We check if is null */
                    customerId?.let {
                        Customer(customerViewModel, navController, customerId)
                    }
                }
                composable("settings") {
                    Settings(settingsViewModel, navController)
                }
                composable("payments") {
                    Payments(customerViewModel)
                }
                composable("qrscanner") {
                    QRSCanner(settingsViewModel, navController)
                }

            }
        }
    }

}
