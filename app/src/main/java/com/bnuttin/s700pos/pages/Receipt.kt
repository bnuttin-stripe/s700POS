package com.bnuttin.s700pos.pages

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.components.StatusButton
import com.bnuttin.s700pos.viewmodels.CartViewModel
import com.bnuttin.s700pos.viewmodels.CheckoutViewModel
import com.bnuttin.s700pos.viewmodels.CustomerViewModel
import com.bnuttin.s700pos.viewmodels.PaymentViewModel
import com.bnuttin.s700pos.R

@Composable
fun Receipt(
    customerViewModel: CustomerViewModel,
    paymentViewModel: PaymentViewModel,
    checkoutViewModel: CheckoutViewModel,
    cartViewModel: CartViewModel,
    navController: NavHostController,
) {
    var email by remember { mutableStateOf("")}
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .padding(top = 70.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 10.dp)
        ) {
            Text(
                "Receipt",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        OutlinedTextField(
            value = email ?: "",
            onValueChange = {
                email = it;
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text("Receipt Email") },
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    checkoutViewModel.sendReceipt(id=checkoutViewModel.paymentIntendId, email=email)
                }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
        )
        Row() {
            StatusButton(
                onClick = {
                    checkoutViewModel.sendReceipt(id=checkoutViewModel.paymentIntendId, email=email)
                },
                status = checkoutViewModel.statusReceipt,
                icon = R.drawable.outline_receipt_24,
                label = "Send Receipt",
                modifier = Modifier.padding(end = 8.dp)
            )
            StatusButton(
                onClick = {
                    cartViewModel.emptyCart()
                    checkoutViewModel.reset()
                    navController.navigate("shop")
                },
                status = "done",
                icon = R.drawable.outline_home_24,
                label = "Done",
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

