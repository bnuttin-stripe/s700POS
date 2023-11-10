package com.bnuttin.s700pos.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.components.PaymentLine
import com.bnuttin.s700pos.components.TopRow
import com.bnuttin.s700pos.viewmodels.AppPreferences
import com.bnuttin.s700pos.viewmodels.PaymentViewModel
import com.example.s700pos.R

@Composable
fun PaymentList(paymentViewModel: PaymentViewModel, navController: NavHostController) {
    var search by remember { mutableStateOf("") }

    TopRow(
        title = "Payments",
        onClick = {
            search = "";
            paymentViewModel.searchPayments("")
        },
        status = "done",
        icon = R.drawable.baseline_refresh_24,
        label = "Refresh",
        modifier = Modifier.padding(start = 10.dp, top = 70.dp, end = 10.dp, bottom = 8.dp),
    )
    OutlinedTextField(
        value = search,
        onValueChange = {
            search = it;
            paymentViewModel.searchPayments("metadata['orderId']:'" + AppPreferences.orderIdPrefix + "-" + search + "'")
        },
        singleLine = true,
        modifier = Modifier
            .padding(start = 20.dp, top = 130.dp, end = 20.dp, bottom = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        leadingIcon = {
            Icon(
                painterResource(R.drawable.baseline_search_24),
                contentDescription = "Search",
                tint = Color.DarkGray
            )
        },
        placeholder = {Text("Order ID")}
    )
    when (paymentViewModel.searchStatus) {
        "done" -> LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            content = {
                items(paymentViewModel.searchPayments.size) { index ->
                    PaymentLine(paymentViewModel.searchPayments[index], navController)
                }
            },
            modifier = Modifier.padding(top = 200.dp, start = 10.dp, end = 10.dp)
        )

        "error" -> Text(
            "Connection error",
            modifier = Modifier.padding(top = 200.dp, start = 10.dp, end = 10.dp)
        )
    }

}