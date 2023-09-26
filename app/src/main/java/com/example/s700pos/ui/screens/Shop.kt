package com.example.s700pos.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.s700pos.ui.components.LoadingImage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shop() {
    val productViewModel: ProductViewModel = viewModel()

    Column(
        modifier = Modifier
            .padding(top = 60.dp)
    ) {
        val productUiState = productViewModel.productUiState
        Text(productUiState.status)
        when (productUiState.status) {
            "loading" -> LoadingImage()
            //"done" -> productUiState.products?.let { it1 -> ProductList(it1) }
            "done" -> Text(productUiState.products?.size.toString())
        }
        //(productUiStateNew.numProducts.toString())
        Button(onClick = { productViewModel.getProducts() }) {
            Text("Reload")
        }
    }

//    Scaffold(
//        floatingActionButtonPosition = FabPosition.End,
//        floatingActionButton = {
//            ExtendedFloatingActionButton(
//                onClick = { /* fab click handler */ }
//            ) {
//                Text("Inc")
//            }
//        },
//        content = {
//            Column(
//                modifier = Modifier
//                    .padding(top = 60.dp)
//            ) {
//                val productUiState = productViewModel.productUiState
//                Text(productUiState.status)
//                when (productUiState.status) {
//                    "loading" -> LoadingImage()
//                    "done" -> productUiState.products?.let { it1 -> ProductList(it1) }
//                }
//                Button(onClick = { productViewModel.getProducts() }) {
//                    Text("Reload")
//                }
//            }
//        }
//    )
}