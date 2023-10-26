package com.bnuttin.s700pos.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnuttin.s700pos.components.ProductCard
import com.bnuttin.s700pos.models.CartViewModel
import com.bnuttin.s700pos.models.ProductViewModel
import com.example.s700pos.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Shop(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
) {
    Row(
        modifier = Modifier.padding(start = 10.dp, top = 70.dp, end = 10.dp, bottom = 8.dp)
    ) {
        Text(
            "Shop",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { productViewModel.getProducts() }) {
            when (productViewModel.status) {
                "done" -> Icon(
                    painterResource(R.drawable.baseline_refresh_24),
                    contentDescription = "Shop",
                    tint = Color.DarkGray
                )

                "loading" -> CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )

                "error" -> Icon(
                    painterResource(R.drawable.baseline_cloud_off_24),
                    contentDescription = "Shop",
                    tint = Color.DarkGray
                )
            }
        }
    }
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
            modifier = Modifier.padding(top = 120.dp, start = 10.dp, end = 10.dp))
    }
}
