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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.components.Cart
import com.bnuttin.s700pos.components.TopRow
import com.bnuttin.s700pos.viewmodels.AppPreferences
import com.bnuttin.s700pos.viewmodels.CartViewModel
import com.bnuttin.s700pos.viewmodels.CheckoutViewModel
import com.bnuttin.s700pos.viewmodels.PrefRepository
import com.example.s700pos.R

@Composable
fun Checkout(
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel,
    navController: NavHostController
) {
    val prefRepository = PrefRepository(LocalContext.current)

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

        Cart(cartViewModel)

        Button(
            onClick = {
                val amount = cartViewModel.total * 100
                checkoutViewModel.createPaymentIntent(amount = amount.toInt())
                checkoutViewModel.createPaymentIntentNew(amount = amount.toLong())
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
                Text("Pay")
            }
        }
        //Text("Payment intent client secret: " + checkoutViewModel.currentPaymentIntent.value?.clientSecret ?: "")
        Text("Payment intent client secret: " + checkoutViewModel.currentPayment?.client_secret ?: "")
    }
}