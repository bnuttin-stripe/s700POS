package com.bnuttin.s700pos.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnuttin.s700pos.components.Cart
import com.bnuttin.s700pos.models.CartViewModel
import com.bnuttin.s700pos.models.CheckoutViewModel

@Composable
fun Checkout(cartViewModel: CartViewModel, checkoutViewModel: CheckoutViewModel) {
    Column(
        modifier = Modifier
            .padding(top = 70.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Cart",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
        )
        Cart(cartViewModel)
        Button(
            onClick = {
                val amount = cartViewModel.total * 100
                checkoutViewModel.createPaymentIntent(amount = amount.toInt())
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
                Text("Create PI")
            }
        }
        Text("Payment intent client secret: " + checkoutViewModel.paymentIntent.client_secret)
    }
}