package com.bnuttin.s700pos.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.components.Cart
import com.bnuttin.s700pos.components.TopRow
import com.bnuttin.s700pos.viewmodels.AppPreferences
import com.bnuttin.s700pos.viewmodels.CartViewModel
import com.bnuttin.s700pos.viewmodels.CheckoutViewModel
import com.bnuttin.s700pos.viewmodels.Customer
import com.bnuttin.s700pos.viewmodels.CustomerViewModel
import com.example.s700pos.R

@Composable
fun Checkout(
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel,
    customerViewModel: CustomerViewModel,
    navController: NavHostController,
) {
    var customerEmail by remember { mutableStateOf("") }
    var customerName by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1=checkoutViewModel.statusPaymentIntent){
        if (checkoutViewModel.statusPaymentIntent == "SUCCEEDED")
            navController.navigate("receipt")
    }

    Column(
        modifier = Modifier
            .padding(top = 70.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TopRow(
            title = "Checkout ${AppPreferences.brandName}",
            onClick = { cartViewModel.emptyCart() },
            status = "done",
            icon = R.drawable.baseline_remove_shopping_cart_24,
            label = "Empty Cart",
            modifier = Modifier
        )
        Text(
            "Cart",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Cart(cartViewModel)
        Text(
            "Customer",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 20.dp, bottom = 8.dp)
        )
        Text(
            "Skip for Guest Checkout",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = customerEmail,
            onValueChange = {
                customerEmail = it;
            },
            singleLine = true,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            trailingIcon = {
                if (customerEmail != "") {
                    Icon(
                        painterResource(R.drawable.outline_close_24),
                        contentDescription = "Search",
                        tint = Color.DarkGray,
                        modifier = Modifier.clickable(onClick = {
                            customerEmail = ""
                            customerViewModel.customerCheckoutStatus = ""
                            customerViewModel.customerCheckout = Customer()
                        })
                    )
                }
            },
            placeholder = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    customerViewModel.getCustomerCheckout(customerEmail)
                }),
        )
        if (customerViewModel.customerCheckoutStatus != "" && customerViewModel.customerCheckout.id == "notfound") {
            OutlinedTextField(
                value = customerName ?: "",
                onValueChange = {
                    customerName = it;
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text("Name") },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        customerViewModel.createCustomer(customerName, customerEmail)
                    }),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
                trailingIcon = {
                    Icon(
                        painterResource(R.drawable.outline_person_add_24),
                        contentDescription = "Add",
                        tint = Color.DarkGray,
                        modifier = Modifier.clickable(onClick = {
                            customerViewModel.createCustomer(customerName, customerEmail)
                        })
                    )
                }
            )
        }
        if (customerEmail != "" && customerViewModel.customerCheckout != Customer() && customerViewModel.customerCheckout.id != "notfound") {
            Row(
                modifier = Modifier
                    .background(color = Color.LightGray)
                    .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically

            ){
                Icon(
                    painterResource(R.drawable.person),
                    contentDescription = "Customer",
                    tint = Color.DarkGray,
                    modifier = Modifier.padding(end=8.dp)
                )
                Text(customerViewModel.customerCheckout.name ?: "")
            }

        }
        Button(
            onClick = {
                val amount = cartViewModel.total
                val items = cartViewModel.items
                val customerId = customerViewModel.customerCheckout.id ?: ""
                checkoutViewModel.createPaymentIntent(amount, items, customerId)
            },
            shape = RoundedCornerShape(size = 6.dp),
            enabled = cartViewModel.total > 0,
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
        ) {
            if (checkoutViewModel.statusPaymentIntent == "loading") {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            } else {
                Text("Collect Payment")
            }
        }
    }
}