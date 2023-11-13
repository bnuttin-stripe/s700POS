package com.bnuttin.s700pos.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bnuttin.s700pos.viewmodels.Payment
import com.bnuttin.s700pos.viewmodels.PaymentMetadata
import com.example.s700pos.R

@Composable
fun PaymentLine(
    payment: Payment,
    navController: NavController,
) {
    Column(
    ) {
        ListItem(
            headlineContent = {
                Row() {
                    Text(
                        text = payment.metadata?.orderId ?: "",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = payment.amount?.let { FormattedPriceLabel(it.toDouble()) } ?: "",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(textDecoration = if (payment.latest_charge?.refunded == true) TextDecoration.LineThrough else TextDecoration.None),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            supportingContent = {
                Row(
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(FormattedDate(payment.created ?: 0))
                    if (payment.metadata?.channel == "online") {
                        Text(" @ Online")
                    } else {
                        Text((" @ " + payment.metadata?.store) ?: "")
                    }
                }
            },
            leadingContent = {
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
                )
            },
            modifier = Modifier
                .clickable(onClick = {
                    navController.navigate("payment/" + payment.id)
                })
        )
        HorizontalDivider()
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PaymentLinePreview() {
    PaymentLine(
        payment = Payment(
            id = "pi_3O83bmFyN0fE9mUH175qt0e4",
            amount = 2600,
            amount_received = 2600,
            description = "Hello world",
            status = "Successful",
            metadata = PaymentMetadata(
                bopis = "Pending",
                orderId = "PRESS-123",
                channel = "offline",
                items = "price_1O7PqAFyN0fE9mUHlNJItP0r, price_1O7PpWFyN0fE9mUHjZTlTRmo",
                store = "Chicago Lincoln Park"
            ),
            created = 1698941905
        ), navController = NavController(LocalContext.current)
    )
}