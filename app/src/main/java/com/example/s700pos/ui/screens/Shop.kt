package com.example.s700pos.ui.screens

import android.annotation.SuppressLint
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.s700pos.ui.components.ErrorImage
import com.example.s700pos.ui.components.LoadingImage
import com.example.s700pos.ui.components.ProductList
import com.example.s700pos.ui.models.ProductViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop() {
    val productViewModel: ProductViewModel = viewModel()

    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* fab click handler */ }
            ) {
                Text("Inc")
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(top = 70.dp, start = 10.dp, end = 10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                val productUiState = productViewModel.productUiState
                when (productUiState.status) {
                    "loading" -> LoadingImage()
                    "done" -> ProductList(productUiState.products)
                    "error" -> ErrorImage()
                }
                //(productUiStateNew.numProducts.toString())
                Button(onClick = {
                    productViewModel.setLoading()
                    productViewModel.getProducts()
                }) {
                    Text(productUiState.status)
                }
            }
        }
    )
}