package com.bnuttin.s700pos.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnuttin.s700pos.api.PrefRepository
import com.bnuttin.s700pos.components.Cart
import com.bnuttin.s700pos.viewmodels.CartViewModel
import com.bnuttin.s700pos.viewmodels.CheckoutViewModel
import com.example.s700pos.R

@Composable
fun Checkout(
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel,
) {
    val prefRepository = PrefRepository(LocalContext.current)

    Column(
        modifier = Modifier
            .padding(top = 70.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Checkout",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(weight = 1f))
            IconButton(onClick = { cartViewModel.emptyCart() }) {
                Icon(
                    painterResource(R.drawable.baseline_remove_shopping_cart_24),
                    contentDescription = "Back",
                    tint = Color.DarkGray
                )
            }
        }
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