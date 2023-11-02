package com.bnuttin.s700pos.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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
    ) {
        TopRow(
            title = "Order Details",
            onClick = { navController.navigate("customer/" + customerViewModel.customer.id) },
            status = paymentViewModel.status,
            icon = R.drawable.outline_arrow_back_24,
            label = "Back",
            modifier = Modifier
        )
        Row {
            if (paymentViewModel.paymentIntent.metadata?.bopis == "pending") {
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
                icon = R.drawable.baseline_arrow_upward_24,
                label = "Refund",
                modifier = Modifier
            )
        }
    }
}

