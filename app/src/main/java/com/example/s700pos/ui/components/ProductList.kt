package com.example.s700pos.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.s700pos.model.Product

@Composable
fun ProductList(products: List<Product>?, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),

    ) {
        if (products.isNullOrEmpty()) {
            Text("No products found")
        } else {
            products.forEach() {
                ProductCard(it)
            }
        }
    }
}