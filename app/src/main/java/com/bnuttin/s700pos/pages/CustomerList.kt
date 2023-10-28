package com.bnuttin.s700pos.pages

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.components.CustomerCard
import com.bnuttin.s700pos.viewmodels.CustomerViewModel
import com.example.s700pos.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CustomerList(customerViewModel: CustomerViewModel, navController: NavHostController) {
    var context = LocalContext.current
    var emailSearch by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var name by remember { mutableStateOf( "") }
//    val id by remember { mutableStateOf("") }
    val controller = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier.padding(start = 10.dp, top = 70.dp, end = 10.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Customers",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.weight(weight = 1f))
        IconButton(onClick = { customerViewModel.searchCustomers("") }) {
            when (customerViewModel.status) {
                "done" -> Icon(
                    painterResource(R.drawable.baseline_refresh_24),
                    contentDescription = "Refresh",
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
    when (customerViewModel.status) {
        "done" -> LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            content = {
                items(customerViewModel.customers.size) { index ->
                    CustomerCard(customerViewModel.customers[index], navController)
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