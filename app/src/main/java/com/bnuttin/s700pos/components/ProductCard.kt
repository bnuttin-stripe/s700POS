package com.bnuttin.s700pos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bnuttin.s700pos.models.CartViewModel
import com.bnuttin.s700pos.models.Product

@Composable
fun ProductCard(product: Product, cartViewModel: CartViewModel) {
    Card(
        shape = RectangleShape,
        modifier = Modifier
            .padding(2.dp)
            .aspectRatio(1f)
            .clickable(onClick = {
                cartViewModel.addToCart(product = product)
            })
    ) {
        Box(){
            AsyncImage(
                model = product.image,
                contentDescription = "Product image",
                modifier = Modifier.width(300.dp)
            )
            Text(
                text = FormattedPriceLabel(product.price),
                color = White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(top = 2.dp, bottom = 2.dp, start = 5.dp, end = 5.dp),
                fontSize = 14.sp
            )
        }
    }
}
