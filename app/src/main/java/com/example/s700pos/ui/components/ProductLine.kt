package com.example.s700pos.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.s700pos.ui.models.Product

@Composable
fun ProductLine(product: Product, modifier: Modifier) {
    Column(

    ) {
        Row(
            modifier = modifier
        ) {
            Text(
                product.name,
                fontSize = 20.sp,
                modifier = Modifier.weight(.75f)
            )
            Text(
                FormattedPriceLabel(product.price),
                fontSize = 20.sp,
                modifier = Modifier.weight(.25f),
                textAlign = TextAlign.End
            )
        }
        Divider()
    }
}
