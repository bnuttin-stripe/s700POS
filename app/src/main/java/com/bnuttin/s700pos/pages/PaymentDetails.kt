package com.bnuttin.s700pos.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.components.PaymentMethod
import com.bnuttin.s700pos.components.PrettyButton
import com.bnuttin.s700pos.components.TopRow
import com.bnuttin.s700pos.viewmodels.CustomerViewModel
import com.bnuttin.s700pos.viewmodels.PaymentViewModel
import com.example.s700pos.R

@Composable
fun PaymentDetails(
    customerViewModel: CustomerViewModel,
    paymentViewModel: PaymentViewModel,
    navController: NavHostController,
    paymentId: String,
) {
    LaunchedEffect(key1 = true) {
        paymentViewModel.getPayment(paymentId)
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
            .verticalScroll(rememberScrollState()),
        content = function(customerViewModel, navController, paymentViewModel, paymentId)
    )
}

@Composable
private fun function(
    customerViewModel: CustomerViewModel,
    navController: NavHostController,
    paymentViewModel: PaymentViewModel,
    paymentId: String,
): @Composable() (ColumnScope.() -> Unit) =
    {
        val payment = paymentViewModel.payment

        TopRow(
            title = "Payments Details",
            onClick = {
                if ((customerViewModel.customer.id ?: "") === "") {
                    navController.navigate("payments")
                } else {
                    navController.navigate("customer/" + customerViewModel.customer.id)
                }
            },
            status = paymentViewModel.status,
            icon = R.drawable.outline_arrow_back_24,
            label = "Back",
            modifier = Modifier
        )
        Column(){
            Text(
                ("ID: " + payment.id) ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                "Status: " + payment.status ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                "Created: " + payment.created ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 8.dp)
            )

        }
        Row {
            if (paymentViewModel.payment.metadata?.bopis == "pending") {
                PrettyButton(
                    onClick = { paymentViewModel.bopisPickedUp(paymentId) },
                    status = paymentViewModel.status,
                    icon = R.drawable.baseline_check_24,
                    label = "Picked Up",
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            PrettyButton(
                onClick = { },
                status = paymentViewModel.status,
                icon = R.drawable.baseline_subdirectory_arrow_left_24,
                label = "Refund",
                modifier = Modifier
            )
        }
        PaymentMethod()
    }

