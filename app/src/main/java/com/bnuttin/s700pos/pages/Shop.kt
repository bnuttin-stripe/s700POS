package com.bnuttin.s700pos.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bnuttin.s700pos.components.ProductCard
import com.bnuttin.s700pos.components.TopRow
import com.bnuttin.s700pos.viewmodels.CartViewModel
import com.bnuttin.s700pos.viewmodels.ProductViewModel
import com.example.s700pos.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Shop(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
) {
    TopRow(
        title = "Shop ",
        onClick = { productViewModel.getProducts() },
        status = productViewModel.status,
        icon = R.drawable.baseline_refresh_24,
        label = "Refresh",
        modifier = Modifier.padding(start = 10.dp, top = 70.dp, end = 10.dp, bottom = 8.dp),
    )
    when (productViewModel.status) {
        "done" -> LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            content = {
                items(productViewModel.products.size) { index ->
                    ProductCard(productViewModel.products[index], cartViewModel)
                }
            },
            modifier = Modifier.padding(top = 120.dp, start = 10.dp, end = 10.dp)
        )

        "error" -> Text(
            "Connection error",
            modifier = Modifier.padding(top = 120.dp, start = 10.dp, end = 10.dp)
        )
    }
}
