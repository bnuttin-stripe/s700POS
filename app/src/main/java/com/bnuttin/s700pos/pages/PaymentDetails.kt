package com.bnuttin.s700pos.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.viewmodels.CustomerViewModel
import com.bnuttin.s700pos.viewmodels.PaymentViewModel
import com.example.s700pos.R

@Composable
fun PaymentDetails(
    customerViewModel: CustomerViewModel,
    paymentViewModel: PaymentViewModel,
    navController: NavHostController,
    paymentId: String
) {
    LaunchedEffect(key1 = true) {
        paymentViewModel.getPaymentIntent(paymentId)
    }

//    val payments = paymentViewModel.customerPayments
//    var selectedTab by remember { mutableStateOf(0) }
//    // TODO put the main customer data into remmeber blocks so we don't requery every time we go to the page?
//
//    LaunchedEffect(key1 = true) {
//        customerViewModel.resetCustomer()
//        paymentViewModel.resetCustomerPayments()
//        customerViewModel.getCustomer(id)
//        paymentViewModel.getCustomerPayments(id)
//    }




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
                "Payment Details",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(weight = 1f))
            IconButton(onClick = { navController.navigate("customer/" + customerViewModel.customer.id) }) {
                when (paymentViewModel.status) {
                    "done" -> Icon(
                        painterResource(R.drawable.outline_arrow_back_24),
                        contentDescription = "Refresh",
                        tint = Color.DarkGray
                    )

                    "loading" -> CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )

                    "error" -> Icon(
                        painterResource(R.drawable.baseline_cloud_off_24),
                        contentDescription = "Error",
                        tint = Color.DarkGray
                    )
                }
            }

        }
        Text(paymentId)
        Text("HEllo" + paymentViewModel.paymentIntent.metadata?.bopis)
        Button(
            onClick = {
                paymentViewModel.bopisPickedUp(paymentId)
                //paymentViewModel.getPaymentIntent(paymentId)
            },
            shape = RoundedCornerShape(size = 6.dp),
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
        ) {
            if (paymentViewModel.status == "loading") {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            } else {
                Text("Mark Picked Up")
            }
        }
        //paymentViewModel.bopisPickedUp(PaymentIntent(metadata = PaymentIntentMetadata(bopis = "Picked Up")))
    }
}
