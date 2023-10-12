package com.example.s700pos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.s700pos.ui.models.CartViewModel
import com.example.s700pos.ui.models.Product

@Composable
fun ProductCard(product: Product, cartViewModel: CartViewModel) {
    Card(
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .padding(0.dp, 5.dp)
            .clickable(onClick = {
                cartViewModel.addToCart(product = product)
            })
    ) {
        Row(
            modifier = Modifier
                .padding(6.dp)
                //.background(color = Color.LightGray)
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = "Product image",
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )
            Column(
                modifier = Modifier
                    .padding(10.dp, 0.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text("Price: " + FormattedPriceLabel(product.price))
                }
            }
        }
    }
}
