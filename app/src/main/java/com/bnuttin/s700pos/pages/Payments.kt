package com.bnuttin.s700pos.pages

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.bnuttin.s700pos.models.CustomerViewModel

@Composable
fun Payments(customerViewModel: CustomerViewModel) {
    Text(customerViewModel.customer.name ?: "")

}