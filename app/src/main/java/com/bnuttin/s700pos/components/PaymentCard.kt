package com.bnuttin.s700pos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bnuttin.s700pos.viewmodels.Customer
import com.bnuttin.s700pos.viewmodels.PaymentIntent
import com.bnuttin.s700pos.viewmodels.PaymentViewModel
import com.example.s700pos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentCard(
    customer: Customer,
    payment: PaymentIntent,
    paymentViewModel: PaymentViewModel,
    navController: NavController,
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(
        //verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .height(48.dp)
                //.background(MaterialTheme.colorScheme.primary)
                .background(Color.LightGray)
                .padding(start = 8.dp, end = 8.dp)
                .clickable(onClick = {
                    navController.navigate("payment/" + payment.id)
                })
        ) {
            Icon(
                painterResource(
                    if (payment.metadata?.channel == "online") {
                        R.drawable.outline_language_24
                    } else {
                        R.drawable.outline_store_24
                    }
                ),
                contentDescription = "Channel",
                modifier = Modifier
                    .padding(end = 4.dp)
            )
            Text(
                text = "Nov 2",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 4.dp)
            )
            Text(
                text = payment.metadata?.order ?: "",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = payment.amount?.let { FormattedPriceLabel(it.toDouble()) } ?: "",
                fontSize = 18.sp
            )
        }
    }


//
//    if (showBottomSheet) {
//        ModalBottomSheet(onDismissRequest = { /* Executed when the sheet is dismissed */ }) {
//            Text("Refund functionality goes here")
//        }
//    }
}
