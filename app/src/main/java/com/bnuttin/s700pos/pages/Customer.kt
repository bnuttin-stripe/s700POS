package com.bnuttin.s700pos.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.viewmodels.CustomerViewModel
import com.example.s700pos.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Customer(customerViewModel: CustomerViewModel, navController: NavHostController, id: String) {
    var payments = customerViewModel.customer.payments
    var selectedTab by remember { mutableStateOf(0) }


    LaunchedEffect(key1 = true) {
        customerViewModel.resetCustomer()
        customerViewModel.getCustomer(id)
    }

    Column(
        modifier = Modifier
            .padding(top = 70.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Customer Details",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(weight = 1f))
            IconButton(onClick = { navController.navigate("customers") }) {
                when (customerViewModel.status) {
                    "done" -> Icon(
                        painterResource(R.drawable.outline_arrow_back_24),
                        contentDescription = "Back",
                        tint = Color.DarkGray
                    )

                    "loading" -> CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
            }
        }
        Text(
            customerViewModel.customer.email ?: "",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
        )
        OutlinedTextField(
            value = customerViewModel.customer.name ?: "",
            onValueChange = {
            },
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = customerViewModel.customer.email ?: "",
            onValueChange = {
            },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        HorizontalDivider()
        SecondaryTabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("BOPIS") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Past Payments") }
            )
        }
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Secondary tab ${selectedTab + 1} selected",
            style = MaterialTheme.typography.bodyLarge
        )
        Column() {
            if (payments.isNullOrEmpty()) {
                Text("No previous payments")
            } else {
                payments.forEach() { payment ->
                    Text(payment.id ?: "ID Missing")
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

        }
    }
}
