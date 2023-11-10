package com.bnuttin.s700pos.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.components.FormattedPriceLabel
import com.bnuttin.s700pos.components.PaymentLine
import com.bnuttin.s700pos.components.TopRow
import com.bnuttin.s700pos.viewmodels.CustomerViewModel
import com.bnuttin.s700pos.viewmodels.PaymentViewModel
import com.example.s700pos.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CustomerDetails(
    customerViewModel: CustomerViewModel,
    paymentViewModel: PaymentViewModel,
    navController: NavHostController,
    id: String,
) {
    //val payments = paymentViewModel.customerPayments
    val payments = paymentViewModel.searchPayments
    val customer = customerViewModel.customer

    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = true) {
        customerViewModel.resetCustomer()
        paymentViewModel.resetCustomerPayments()
        customerViewModel.getCustomer(id)
        paymentViewModel.getCustomerPayments(id)
    }

    Column(
        modifier = Modifier
            .padding(top = 70.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TopRow(
            title = "Customer Details",
            onClick = { navController.navigate("customers") },
            status = customerViewModel.status,
            icon = R.drawable.outline_arrow_back_24,
            label = "Back",
            modifier = Modifier
        )
        Row(
            modifier = Modifier.padding(bottom = 12.dp)
        ){
            Text(
                "Name: ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                customer.name ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Row(
            modifier = Modifier.padding(bottom = 12.dp)
        ){
            Text(
                "Email: ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                customer.email ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Row(
            modifier = Modifier.padding(bottom = 12.dp)
        ){
            Text(
                "Lifetime Payments: ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "" + customer.ltv?.let{ FormattedPriceLabel(it.toDouble()) },
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Row(
            modifier = Modifier.padding(bottom = 12.dp)
        ){
            Text(
                "Lifetime Purchases: ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                customer.payments.toString() ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }

//        OutlinedTextField(
//            value = customerViewModel.customer.name ?: "",
//            onValueChange = {
//            },
//            label = { Text("Name") },
//            keyboardOptions = KeyboardOptions(
//                capitalization = KeyboardCapitalization.Words
//            ),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 8.dp)
//        )
//        OutlinedTextField(
//            value = customerViewModel.customer.email ?: "",
//            onValueChange = {
//            },
//            label = { Text("Email") },
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Email
//            ),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 8.dp)
//        )
//        HorizontalDivider()
        SecondaryTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Pick Up in Store") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Past Payments") }
            )
        }
        if (selectedTab == 0) {
            Column(
            ) {
                if (payments.isNullOrEmpty()) {
                    Text("No pending BOPIS orders")
                } else {
                    payments.forEach() { payment ->
                        if (payment.metadata?.bopis == "pending") {
                            PaymentLine(
                                payment,
                                navController
                            )
                        }
                    }
                }
            }
        } else {
            Column(
            ) {
                if (payments.isNullOrEmpty()) {
                    Text("No previous payments")
                } else {
                    payments.forEach() { payment ->
                        if (payment.metadata?.bopis != "pending") {
                            PaymentLine(
                                payment,
                                navController
                            )
                        }
                    }
                }
            }
        }
    }
}

//var context = LocalContext.current
//var emailSearch by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var name by remember { mutableStateOf( "") }
//    val id by remember { mutableStateOf("") }
//val controller = LocalSoftwareKeyboardController.current

//    Row(
//        modifier = Modifier.padding(start = 10.dp, top = 70.dp, end = 10.dp, bottom = 8.dp)
//    ) {
//        Text(
//        "Customers",
//        fontSize = 28.sp,
//        fontWeight = FontWeight.Bold,
//    )
//    Spacer(modifier = Modifier.weight(weight = 1f))
//    IconButton(onClick = { customerViewModel.searchCustomers("") }) {
//        when (customerViewModel.status) {
//            "done" -> Icon(
//                painterResource(R.drawable.baseline_refresh_24),
//                contentDescription = "Refresh",
//                tint = Color.DarkGray
//            )
//
//            "loading" -> CircularProgressIndicator(
//                strokeWidth = 2.dp,
//                modifier = Modifier.size(ButtonDefaults.IconSize)
//            )
//
//            "error" -> Icon(
//                painterResource(R.drawable.baseline_cloud_off_24),
//                contentDescription = "Error",
//                tint = Color.DarkGray
//            )
//        }
//    }

//    Column(
//        modifier = Modifier
//            .padding(top = 70.dp, start = 10.dp, end = 10.dp)
//            .verticalScroll(rememberScrollState())
//            .pointerInput(Unit) {
//                detectTapGestures(onTap = {
//                    controller?.hide()
//                })
//            }
//    ) {
//        Text(
//            text = "Customer Lookup",
//            fontSize = 28.sp,
//            fontWeight = FontWeight.Bold)
//        OutlinedTextField(
//            value = emailSearch,
//            onValueChange = { emailSearch = it },
//            label = { Text("Email") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Email
//            )
//        )
//        Text(customerViewModel.customer.name ?: "pending")
//        Button(
//            onClick = {
//                controller?.hide();
//                CoroutineScope(Dispatchers.Default).launch{
//                    customerViewModel.searchCustomer(email = emailSearch)
//                }
//                name = customerViewModel.customer.name ?: "dsdfgdfg";
//                email = customerViewModel.customer.email ?: "";
//            },
//            shape = RoundedCornerShape(size = 6.dp),
//            enabled = emailSearch.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailSearch)
//                .matches(),
//            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
//        ) {
//            if (customerViewModel.status == "loading") {
//                CircularProgressIndicator(
//                    color = Color.White,
//                    strokeWidth = 2.dp,
//                    modifier = Modifier.size(ButtonDefaults.IconSize)
//                )
//            } else {
//                Text("Search")
//            }
//        }
//
//        Text(
//            text = "Customer Details" + id,
//            fontSize = 28.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
//        )
//        // Customer Name
//        OutlinedTextField(
//            value = name ?: "",
//            onValueChange = { name = it },
//            label = { Text("Name") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Text,
//                capitalization = KeyboardCapitalization.Words
//            )
//        )
//        OutlinedTextField(
//            value = email ?: "",
//            onValueChange = { email = it },
//            label = { Text("Email") },
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Email
//            )
//        )
//        Button(
//            onClick = {
//                controller?.hide();
//                customerViewModel.updateCustomer(id = customerViewModel.customer.id ?: "", name = name, email = email)
//            },
//            shape = RoundedCornerShape(size = 6.dp),
//            //enabled = email.isNotEmpty() && name.isNotEmpty(),
//            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
//        ) {
//            if (customerViewModel.status == "loading") {
//                CircularProgressIndicator(
//                    color = Color.White,
//                    strokeWidth = 2.dp,
//                    modifier = Modifier.size(ButtonDefaults.IconSize)
//                )
//            } else {
//                Text("Update")
//            }
//        }
//
//        Button(
//            onClick = {
//                navController.navigate("payments")
//            },
//            shape = RoundedCornerShape(size = 6.dp),
////            enabled = emailSearch.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailSearch)
////                .matches(),
//            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
//        ) {
//            Text("Payments")
//        }

//        NavHost(navController = navController, startDestination = "customer") {
//            composable("customer") {
//                Customer(customerViewModel, navController)
//            }
//            composable("payments") {
//                Payments(customerViewModel)
//            }
//        }

//        Text(
//            text = "ID: " + customerViewModel.customer.id
//        )
//        if (customerViewModel.customer.id == null) {
//            Text("NOT FOUND")
//        }

