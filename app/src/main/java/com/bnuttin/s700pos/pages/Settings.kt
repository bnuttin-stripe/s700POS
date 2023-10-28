@file:OptIn(ExperimentalMaterial3Api::class)

package com.bnuttin.s700pos.pages

import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.api.SettingsViewModel
import com.example.s700pos.R

//@OptIn(ExperimentalMaterial3Api::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(settingsViewModel: SettingsViewModel, navController: NavHostController) {
    val context = LocalContext.current

//    var sellerName by remember { mutableStateOf(prefRepository.getSellerName()) }
//    var currency by remember { mutableStateOf(prefRepository.getCurrency()) }
//    var backendUrl by remember { mutableStateOf(prefRepository.getBackendUrl()) }
    var sellerName by remember { mutableStateOf(settingsViewModel.getSellerName()) }
    var currency by remember { mutableStateOf(settingsViewModel.getCurrency()) }
    var backendUrl by remember { mutableStateOf(settingsViewModel.getBackendUrl()) }
    var formValid: Boolean by remember { mutableStateOf(false) }

    fun checkForm() {
        formValid = sellerName.isNotEmpty() && Patterns.WEB_URL.matcher(backendUrl).matches();
    }

    Column(
        modifier = Modifier
            .padding(top = 70.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 10.dp)
        ) {
            Text(
                "Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                navController.navigate("qrscanner")
            }) {
                Icon(
                    painterResource(R.drawable.baseline_qr_code_scanner_24),
                    contentDescription = "Scan",
                    tint = Color.DarkGray
                )
            }
            IconButton(onClick = {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse("stripe://settings/"))
                )
            }) {
                Icon(
                    painterResource(R.drawable.baseline_phone_android_24),
                    contentDescription = "Phone",
                    tint = Color.DarkGray
                )
            }
        }
        OutlinedTextField(
            value = sellerName,
            onValueChange = {
                sellerName = it;
                checkForm()
            },
            label = { Text("Seller Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = currency,
            onValueChange = {
                currency = it;
                checkForm()
            },
            label = { Text("Currency") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = backendUrl,
            onValueChange = {
                backendUrl = it;
                checkForm()
            },
            label = { Text("Backend URL") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Button(
            onClick = {
//                prefRepository.setSellerName(sellerName)
//                prefRepository.setCurrency(currency)
//                prefRepository.setBackendUrl(backendUrl)
                settingsViewModel.updateSellerName(sellerName)
                settingsViewModel.updateCurrency(currency)
                settingsViewModel.updateBackendUrl(backendUrl)
                settingsViewModel.settingsSaved = true
            },
            shape = RoundedCornerShape(size = 6.dp),
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp),
            enabled = formValid && !settingsViewModel.settingsSaved
        ) {
            Text("Save")
        }


//        Divider()
//        Button(
//            onClick = {
//                settingsViewModel.getConnectionToken()
//            },
//            shape = RoundedCornerShape(size = 6.dp),
//            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
//        ) {
//            if (settingsViewModel.statusConnectionToken == "loading") {
//                CircularProgressIndicator(
//                    color = Color.White,
//                    strokeWidth = 2.dp,
//                    modifier = Modifier.size(ButtonDefaults.IconSize)
//                )
//            } else {
//                Text("Get Connection Token")
//            }
//        }
//        Text("Connection token: " + settingsViewModel.connectionToken.secret)
    }
}