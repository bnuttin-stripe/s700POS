package com.bnuttin.s700pos.pages

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.models.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Customer(customerViewModel: CustomerViewModel, navController: NavHostController) {
    var emailSearch by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val id by remember { mutableStateOf("") }
    val controller = LocalSoftwareKeyboardController.current

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
        Text(
            text = "Customer Lookup",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = emailSearch,
            onValueChange = { emailSearch = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )
        Text(customerViewModel.customer.name ?: "pending")
        Button(
            onClick = {
                controller?.hide();
                customerViewModel.searchCustomer(email = emailSearch);
                Log.d("BENJI", customerViewModel.customer.name ?: "None");
                name = customerViewModel.customer.name ?: "dsdfgdfg";
                email = customerViewModel.customer.email ?: "";
            },
            shape = RoundedCornerShape(size = 6.dp),
            enabled = emailSearch.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailSearch)
                .matches(),
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
        ) {
            if (customerViewModel.status == "loading") {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            } else {
                Text("Search")
            }
        }

        Text(
            text = "Customer Details" + id,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
        )
        // Customer Name
        OutlinedTextField(
            value = name ?: "",
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            )
        )
        OutlinedTextField(
            value = email ?: "",
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )
        Button(
            onClick = {
                controller?.hide();
                customerViewModel.updateCustomer(id = customerViewModel.customer.id ?: "", name = "Bob Jones", email = "hello@B.com")
            },
            shape = RoundedCornerShape(size = 6.dp),
            //enabled = email.isNotEmpty() && name.isNotEmpty(),
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
        ) {
            if (customerViewModel.status == "loading") {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            } else {
                Text("Update")
            }
        }

        Button(
            onClick = {
                navController.navigate("payments")
            },
            shape = RoundedCornerShape(size = 6.dp),
//            enabled = emailSearch.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailSearch)
//                .matches(),
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
        ) {
            Text("Payments")
        }

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