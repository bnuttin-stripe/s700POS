package com.bnuttin.s700pos.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnuttin.s700pos.viewmodels.CartViewModel

//import com.bnuttin.s700pos.viewmodels.CartViewModel

@Composable
fun Cart(cartViewModel: CartViewModel, modifier: Modifier = Modifier) {
    val items = cartViewModel.items

    Column() {
        if (items.isNullOrEmpty()) {
            Text("No items in cart")
        } else {
            items.forEach() { item ->
                ProductLine(
                    item,
                    modifier = Modifier.padding(start = 0.dp, top = 4.dp, end = 0.dp, bottom = 4.dp)
                )
            }
            Row(
                modifier = Modifier
                    .padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 0.dp)
            ) {
                Text(
                    "Sub-total",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(.75f),
                )
                Text(
                    FormattedPriceLabel(cartViewModel.subTotal),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(.25f),
                    textAlign = TextAlign.End,
                )
            }
            Row(
                modifier = Modifier
                    .padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 0.dp)
            ) {
                Text(
                    "Tax",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(.75f),
                )
                Text(
                    FormattedPriceLabel(cartViewModel.tax),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(.25f),
                    textAlign = TextAlign.End
                )
            }
            Row(
                modifier = Modifier
                    .padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 0.dp)
            ) {
                Text(
                    "Total",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(.75f),
                )
                Text(
                    FormattedPriceLabel(cartViewModel.total),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(.25f),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
