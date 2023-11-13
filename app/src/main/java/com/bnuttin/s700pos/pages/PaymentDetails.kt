package com.bnuttin.s700pos.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.api.POSApi
import com.bnuttin.s700pos.components.FormattedDate
import com.bnuttin.s700pos.components.FormattedPriceLabel
import com.bnuttin.s700pos.components.PaymentMethod
import com.bnuttin.s700pos.components.StatusButton
import com.bnuttin.s700pos.components.TopRow
import com.bnuttin.s700pos.viewmodels.CheckoutViewModel
import com.bnuttin.s700pos.viewmodels.CustomerViewModel
import com.bnuttin.s700pos.viewmodels.Payment
import com.bnuttin.s700pos.viewmodels.PaymentMethod
import com.bnuttin.s700pos.viewmodels.PaymentViewModel
import com.bnuttin.s700pos.viewmodels.ProductViewModel
import com.example.s700pos.R

@Composable
fun PaymentDetails(
    customerViewModel: CustomerViewModel,
    paymentViewModel: PaymentViewModel,
    productViewModel: ProductViewModel,
    checkoutViewModel: CheckoutViewModel,
    navController: NavHostController,
    paymentId: String,
) {
    val payment = paymentViewModel.currentPayment
    var cardVerified by remember { mutableStateOf(false) }
    var scannedPaymentMethod by remember { mutableStateOf(PaymentMethod()) }

    LaunchedEffect(key1 = true) {
        paymentViewModel.getPayment(paymentId)
        checkoutViewModel.setupIntentPMId = ""
    }

    LaunchedEffect(key1 = checkoutViewModel.setupIntentPMId) {
        if (!checkoutViewModel.setupIntentPMId.isNullOrEmpty()) {
            scannedPaymentMethod =
                POSApi.payment.getPaymentMethod(checkoutViewModel.setupIntentPMId)
            cardVerified =
                scannedPaymentMethod.card_present?.fingerprint == payment.payment_method?.card_present?.fingerprint
        }
    }

//    LaunchedEffect(key1 = scannedPaymentMethod){
//        Log.d("BENJI", scannedPaymentMethod.card_present?.fingerprint ?: "No fingerprint on scanned card")
//        Log.d("BENJI", payment.payment_method?.card_present?.fingerprint ?: "No fingerprint on card used for purchase")
//        cardVerified = scannedPaymentMethod.card_present?.fingerprint == payment.payment_method?.card_present?.fingerprint
//    }

    Column(
        modifier = Modifier
            .padding(top = 70.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // TODO show smart status in a badge (e.g. if partial refund)
        // TODO change action buttons to regular buttons
        // TODO add details about channel where purchase took place - and field for bopis vs shipped vs picked up
        // TODO back button clears out current payment

        TopRow(
            title = "Payment Details",
            onClick = {
                paymentViewModel.currentPayment = Payment()
                if ((customerViewModel.customer.id ?: "") === "") {
                    navController.navigate("payments")
                } else {
                    navController.navigate("customer/" + customerViewModel.customer.id)
                }
            },
            status = paymentViewModel.currentPaymentStatus,
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
                    if (payment.latest_charge?.refunded == true) "Refunded" else "Success",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    "Amount: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    if (payment.latest_charge?.amount_refunded != null) {
                        if (payment.latest_charge.amount_refunded > 0) {
                            payment.latest_charge.amount_captured?.let { FormattedPriceLabel(it.toDouble()) } + " (" + FormattedPriceLabel(
                                payment.latest_charge.amount_refunded.toDouble()
                            ) + " refunded)"
                        } else {
                            payment.latest_charge.amount_captured?.let { FormattedPriceLabel(it.toDouble()) }
                                ?: ""
                        }
                    } else {
                        payment.latest_charge?.amount_captured?.let { FormattedPriceLabel(it.toDouble()) }
                            ?: ""
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    "On: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    FormattedDate(payment.created ?: 0),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    "At: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    if (payment.metadata?.channel == "online") {
                        "Website"
                    } else {
                        payment.metadata?.store ?: "In store"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    "Delivery: ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    if (payment.metadata?.channel == "online") {
                        when (payment.metadata?.bopis) {
                            "none" -> {
                                "Shipped"
                            }

                            "pending" -> {
                                "Pick up in store (pending)"
                            }

                            else -> {
                                "Pick up in store (done)"
                            }
                        }
                    } else {
                        "N/A"
                    },
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
            if (paymentViewModel.currentPayment.metadata?.bopis == "pending") {
                StatusButton(
                    onClick = { paymentViewModel.bopisPickedUp(paymentId) },
                    status = paymentViewModel.bopisStatus,
                    icon = R.drawable.baseline_check_24,
                    label = "Picked Up",
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            if (payment.latest_charge?.refunded == false) {
                StatusButton(
                    onClick = { paymentViewModel.refundPayment(paymentId) },
                    status = paymentViewModel.refundStatus,
                    icon = R.drawable.baseline_subdirectory_arrow_left_24,
                    label = "Refund",
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            if (checkoutViewModel.setupIntentPMId !== "") {
                if (cardVerified) {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(size = 6.dp),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF007C02)
                        ),
                        modifier = Modifier
                            .padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)

                    ) {
                        Icon(
                            painterResource(R.drawable.baseline_check_24),
                            contentDescription = "Verified",
                            tint = Color.White,
                            modifier = Modifier
                                .size(28.dp)
                                .padding(end = 8.dp)
                        )
                        Text("Card Verified")
                    }
                } else {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(size = 6.dp),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFff6701)
                        ),
                        modifier = Modifier
                            .padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)

                    ) {
                        Icon(
                            painterResource(R.drawable.outline_dangerous_24),
                            contentDescription = "Verified",
                            tint = Color.White,
                            modifier = Modifier
                                .size(28.dp)
                                .padding(end = 8.dp)
                        )
                        Text("Card is Different")
                    }
                }
            } else {
                StatusButton(
                    onClick = { checkoutViewModel.createSetupIntent() },
                    status = "done",
                    icon = R.drawable.credit_card,
                    label = "Verify Card",
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}