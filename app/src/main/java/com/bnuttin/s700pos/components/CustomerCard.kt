package com.bnuttin.s700pos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bnuttin.s700pos.viewmodels.Customer
import com.example.s700pos.R

@Composable
fun CustomerCard(customer: Customer, navController: NavController) {
    Column(
        //verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp, start = 0.dp, end = 0.dp)
                .height(48.dp)
                //.background(MaterialTheme.colorScheme.primary)
                .background(Color.LightGray)
                .padding(top = 0.dp, bottom = 0.dp, start = 3.dp, end = 3.dp)
                .clickable(onClick = {
                    navController.navigate("customer/" + customer.id)
                })
        ){
            Icon(
                painterResource(R.drawable.person),
                contentDescription = "Customer",
                modifier = Modifier
                    //.size(36.dp)
                    .padding(end = 4.dp)
            )
            Text(
                text = customer.name ?: "",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = customer.email ?: "",
                fontSize = 18.sp
            )
        }
    }
}
