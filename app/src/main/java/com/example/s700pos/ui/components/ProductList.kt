package com.example.s700pos.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.s700pos.ui.models.CartViewModel
import com.example.s700pos.ui.models.ProductViewModel

@Composable
fun ProductList(productViewModel: ProductViewModel, cartViewModel: CartViewModel, modifier: Modifier = Modifier) {
    val products = productViewModel.products

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),

    ) {
        if (products.isNullOrEmpty()) {
            Text("No products found")
        } else {
            products.forEach() {
                product -> ProductCard(product, cartViewModel)
            }
        }
    }
}