package com.bnuttin.s700pos.pages

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import com.bnuttin.s700pos.api.POSApi
import com.bnuttin.s700pos.components.FormattedDate
import com.bnuttin.s700pos.components.PaymentMethod
import com.bnuttin.s700pos.components.PrettyButton
import com.bnuttin.s700pos.components.TopRow
import com.bnuttin.s700pos.viewmodels.CheckoutViewModel
import com.bnuttin.s700pos.viewmodels.CustomerViewModel
import com.bnuttin.s700pos.viewmodels.PaymentViewModel
import com.bnuttin.s700pos.viewmodels.ProductViewModel
import com.example.s700pos.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

@Composable
fun PaymentDetails(
    customerViewModel: CustomerViewModel,
    paymentViewModel: PaymentViewModel,
    productViewModel: ProductViewModel,
    checkoutViewModel: CheckoutViewModel,
    navController: NavHostController,
    paymentId: String,
) {
    LaunchedEffect(key1 = true) {
        paymentViewModel.getPayment(paymentId)
    }

    LaunchedEffect(key1 = checkoutViewModel.setupIntentPMId){
        val pm = POSApi.payment.getPaymentMethod(checkoutViewModel.setupIntentPMId)
        Log.d("BENJI", pm.toString())
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

    val payment = paymentViewModel.payment
    Log.d("BENJI", productViewModel.products.toString())

    Column(
        modifier = Modifier
            .padding(top = 70.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // TODO show smart status in a badge (e.g. if partial refund)
        // TODO verify the card via SetupIntents
        // TODO show payment method details
        // TODO show items in purchase

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
        Column {
            Row(
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    "ID: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    payment.id ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    "Order ID: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    payment.metadata?.orderId ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    "Status: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    payment.status ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    "Created: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    FormattedDate(payment.created ?: 0),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Row() {
                Text(
                    "Payment Method: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                PaymentMethod(
                    payment.payment_method?.card_present?.brand
                        ?: payment.payment_method?.card?.brand ?: "unknown",
                    payment.payment_method?.card_present?.wallet?.type
                        ?: payment.payment_method?.card?.wallet?.type
                )
                Text(
                    if (payment.payment_method?.card_present == null) {
                        "Exp " + payment.payment_method?.card?.exp_month.toString() + "/" + payment.payment_method?.card?.exp_year.toString()
                    } else {
                        "Exp " + payment.payment_method.card_present.exp_month.toString() + "/" + payment.payment_method.card_present.exp_year.toString()
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
            }
            Text(
                payment.payment_method?.card?.fingerprint
                    ?: payment.payment_method?.card_present?.fingerprint ?: ""
            )
            Text(
                "Items:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            payment.metadata?.items?.split(", ")?.map { item ->
                productViewModel.products.map { product ->
                    if (product.id == item) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(
                                painterResource(R.drawable.baseline_arrow_right_24),
                                contentDescription = "Bullet",
                                tint = Color.DarkGray
                            )
                            Text(
                                product.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
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
        Text(checkoutViewModel.setupIntentPMId)
        PrettyButton(
            onClick = {
                checkoutViewModel.createSetupIntent()
            },
            status = "done",
            icon = R.drawable.baseline_check_24,
            label = "Scan Card",
            modifier = Modifier.padding(end = 8.dp)
        )
        PrettyButton(
            onClick = {
                CoroutineScope(Dispatchers.Default).launch{
                    try {
                        val fingerprint = POSApi.payment.getPaymentMethod(checkoutViewModel.setupIntentPMId)
                        Log.d("BENJI", "Fingerprint: " + fingerprint.toString())
                    } catch (e: IOException) {

                    }
                }
            },
            status = "done",
            icon = R.drawable.baseline_check_24,
            label = "Verify Card",
            modifier = Modifier.padding(end = 8.dp)
        )

    }
}