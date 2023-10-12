package com.example.s700pos.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.s700pos.ui.components.ErrorImage
import com.example.s700pos.ui.components.LoadingImage
import com.example.s700pos.ui.components.ProductList
import com.example.s700pos.ui.models.CartViewModel
import com.example.s700pos.ui.models.ProductViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop(productViewModel: ProductViewModel, cartViewModel: CartViewModel, navController: NavHostController) {
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate("checkout")
                }
            ) {
                Box() {
//                    Icon(
//                        painterResource(R.drawable.shopping_cart),
//                        contentDescription = "Shop",
//                        modifier = Modifier.padding(end = 10.dp)
//                    )
                    Text(cartViewModel.items.size.toString())
                }
            }
        }
    ){
        Column(
            modifier = Modifier
                .padding(top = 70.dp, start = 10.dp, end = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Shop",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
            )
            when (productViewModel.status) {
                "loading" -> LoadingImage()
                "done" -> ProductList(productViewModel, cartViewModel)
                "error" -> ErrorImage()
            }
            Button(onClick = {
                productViewModel.getProducts()
            }) {
                Text(productViewModel.status)

            }
        }
    }
}