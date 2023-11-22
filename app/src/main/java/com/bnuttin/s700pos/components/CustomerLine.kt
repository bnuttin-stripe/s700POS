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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bnuttin.s700pos.viewmodels.Customer
import com.bnuttin.s700pos.R

@Composable
fun CustomerLine(
    customer: Customer,
    navController: NavController
) {
    Column(
    ) {
        ListItem(
            headlineContent = {
                Row() {
                    Text(
                        text = customer.name ?: "",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = FormattedPriceLabel(amount = customer.ltv ?: 0.0),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            supportingContent = {
                Row(
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(customer.email ?: "")
                }
            },
            leadingContent = {
                Icon(
                    painterResource(R.drawable.person),
                    contentDescription = "Person",
                    modifier = Modifier
                )
            },
            modifier = Modifier
                .clickable(onClick = {
                    navController.navigate("customer/" + customer.id)
                })
        )
        HorizontalDivider()
    }

}
