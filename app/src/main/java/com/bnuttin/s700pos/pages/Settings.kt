@file:OptIn(ExperimentalMaterial3Api::class)

package com.bnuttin.s700pos.pages

import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.viewmodels.AppPreferences
import com.bnuttin.s700pos.viewmodels.CartViewModel
import com.bnuttin.s700pos.viewmodels.CustomerViewModel
import com.bnuttin.s700pos.viewmodels.PaymentViewModel
import com.bnuttin.s700pos.viewmodels.ProductViewModel
import com.bnuttin.s700pos.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.io.IOException

@Serializable
data class ValidationResult(
    val app: String? = null
)

fun validateBackend(){
    CoroutineScope(Dispatchers.Default).launch{
        try {

        } catch (e: IOException) {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    cartViewModel: CartViewModel,
    customerViewModel: CustomerViewModel,
    paymentViewModel: PaymentViewModel,
    productViewModel: ProductViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val controller = LocalSoftwareKeyboardController.current

    var storeName by remember { mutableStateOf(AppPreferences.storeName) }
    var sellerName by remember { mutableStateOf(AppPreferences.sellerName) }
    var brandName by remember { mutableStateOf(AppPreferences.brandName) }
    var backendUrl by remember { mutableStateOf(AppPreferences.backendUrl) }
    var currency by remember { mutableStateOf(AppPreferences.currency) }
    var orderIdPrefix by remember { mutableStateOf(AppPreferences.orderIdPrefix)}
    var taxPercentage by remember { mutableStateOf((AppPreferences.taxPercentage))}

    var formValid: Boolean by remember { mutableStateOf(false) }
    //var backendValidationResult: ValidationResult by remember { mutableStateOf(ValidationResult(app = "none")) }
    //var backendValid: Boolean by remember { mutableStateOf(true) }
    var settingsSaved: Boolean by remember { mutableStateOf(true) }

    val currencyOptions = listOf("USD", "EUR", "GBP")
    var currencyDropdownExpanded by remember { mutableStateOf(false) }
    var selectedCurrencyText by remember { mutableStateOf(currencyOptions[0]) }

    fun checkForm() {
        settingsSaved = false
        formValid = Patterns.WEB_URL.matcher(backendUrl).matches();
        // TODO add validation of backend URL
        formValid = true
    }

    fun resetReload() {
        cartViewModel.emptyCart()
        customerViewModel.searchCustomers("")
        paymentViewModel.searchPayments("")
        productViewModel.getProducts()
    }

//    fun validateEndpoint() {
//        CoroutineScope(Dispatchers.Default).launch{
//            try {
//                backendValidationResult = POSApi.settings.validateBackend()
//                backendValid = (backendValidationResult.app ?: "") == "OK"
//                //Log.d("S700POS", backendValidationResult.app ?: "Not Good")
//            } catch (e: IOException) {
//                backendValid = false
//                //Log.d("S700POS", e.toString())
//            }
//        }
//    }

    fun saveSettings() {
        AppPreferences.storeName = storeName
        AppPreferences.sellerName = sellerName
        AppPreferences.brandName = brandName
        AppPreferences.backendUrl = backendUrl
        AppPreferences.currency = currency
        AppPreferences.orderIdPrefix = orderIdPrefix
        AppPreferences.taxPercentage = taxPercentage
        settingsSaved = true
        resetReload()
    }

    Column(
        modifier = Modifier
            .padding(top = 70.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    controller?.hide()
                })
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 10.dp)
        ) {
            Text(
                "Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                navController.navigate("qrscanner")
            }) {
                Icon(
                    painterResource(R.drawable.baseline_qr_code_scanner_24),
                    contentDescription = "Scan",
                    tint = Color.DarkGray
                )
            }
            IconButton(onClick = {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse("stripe://settings/"))
                )
            }) {
                Icon(
                    painterResource(R.drawable.baseline_phone_android_24),
                    contentDescription = "Phone",
                    tint = Color.DarkGray
                )
            }
        }
        OutlinedTextField(
            value = storeName ?: "",
            onValueChange = {
                storeName = it;
                checkForm()
            },
            label = { Text("Store Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)

        )
        OutlinedTextField(
            value = sellerName ?: "",
            onValueChange = {
                sellerName = it;
                checkForm()
            },
            label = { Text("Seller Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = brandName ?: "",
            onValueChange = {
                brandName = it;
                checkForm()
            },
            label = { Text("Brand Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = backendUrl ?: "",
            onValueChange = {
                backendUrl = it;
                checkForm()
            },
            label = { Text("Backend URL") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
//        ExposedDropdownMenuBox(
//            expanded = currencyDropdownExpanded,
//            onExpandedChange = { currencyDropdownExpanded = !currencyDropdownExpanded },
//        ) {
//            OutlinedTextField(
//                modifier = Modifier
//                    .menuAnchor()
//                    .fillMaxWidth()
//                    .padding(bottom = 8.dp)
//                ,
//                readOnly = true,
//                value = selectedCurrencyText,
//                onValueChange = {  },
//                label = { Text("Currency") },
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyDropdownExpanded) },
//            )
//            ExposedDropdownMenu(
//                expanded = currencyDropdownExpanded,
//                onDismissRequest = { currencyDropdownExpanded = false },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                currencyOptions.forEach { selectionOption ->
//                    DropdownMenuItem(
//                        text = { Text(selectionOption) },
//                        onClick = {
//                            selectedCurrencyText = selectionOption
//                            currencyDropdownExpanded = false
//                            currency = selectionOption
//                            checkForm()
//                        },
//                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
//                    )
//                }
//            }
//        }
        OutlinedTextField(
            value = orderIdPrefix ?: "",
            onValueChange = {
                orderIdPrefix = it;
                checkForm()
            },
            label = { Text("Order ID Prefix") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = taxPercentage.toString() ?: "",
            onValueChange = {
                taxPercentage = it.toFloat();
                checkForm()
            },
            label = { Text("Tax Percentage") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Button(
            onClick = {
                saveSettings()
            },
            shape = RoundedCornerShape(size = 6.dp),
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp),
            enabled = formValid && !settingsSaved
        ) {
            Text("Save")
        }
//        Button(
//            onClick = {
//                validateEndpoint()
//            },
//            shape = RoundedCornerShape(size = 6.dp),
//            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp),
//        ) {
//            Text("Validate Endpoint")
//        }
    }
}