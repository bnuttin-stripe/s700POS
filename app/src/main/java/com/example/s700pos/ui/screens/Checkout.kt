package com.example.s700pos.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.s700pos.ui.models.CartViewModel

@Composable
fun Checkout(cartViewModel: CartViewModel
) {
    //val cartViewModel: CartViewModel = viewModel()

    Column(
        modifier = Modifier
            .padding(top = 60.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Checkout")
        Text(cartViewModel.items.size.toString() + " items in cart")
    }
}