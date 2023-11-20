package com.bnuttin.s700pos.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.components.CustomerLine
import com.bnuttin.s700pos.components.TopRow
import com.bnuttin.s700pos.viewmodels.CustomerViewModel
import com.example.s700pos.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CustomerList(customerViewModel: CustomerViewModel, navController: NavHostController) {
    var search by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
// TODO Search should iterate through current list of items with filter() instead of a call to the backend


    TopRow(
        title = "Customers",
        onClick = {
            search = "";
            customerViewModel.searchCustomers("")
        },
        status = customerViewModel.customerLookupStatus,
        icon = R.drawable.baseline_refresh_24,
        label = "Refresh",
        modifier = Modifier.padding(start = 10.dp, top = 70.dp, end = 10.dp, bottom = 8.dp),
    )
    OutlinedTextField(
        value = search,
        onValueChange = {
            search = it;
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
                tint = Color.DarkGray,
                modifier = Modifier.clickable(onClick = {
                    customerViewModel.searchCustomers(search)
                })
            )
        },
        placeholder = {Text("Name or Email")},
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                customerViewModel.searchCustomers(search)
            }),
        trailingIcon = {
            if (search != "") {
                Icon(
                    painterResource(R.drawable.outline_close_24),
                    contentDescription = "Search",
                    tint = Color.DarkGray,
                    modifier = Modifier.clickable(onClick = {
                        search = ""
                        customerViewModel.searchCustomers("")
                    })
                )
            }
        },
    )
    when (customerViewModel.customerLookupStatus) {
        "done" -> LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            content = {
                items(customerViewModel.customers.size) { index ->
                    CustomerLine(customerViewModel.customers[index], navController)
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