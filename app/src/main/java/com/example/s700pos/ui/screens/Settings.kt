@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.s700pos.ui.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings() {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val shopName = remember { mutableStateOf(preferencesManager.getData("shopName", "POS App")) }
    val backendUrl = remember { mutableStateOf(preferencesManager.getData("backendUrl", "dd")) }

    Column(
        modifier = Modifier
            .padding(top = 60.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Text(
            "Settings",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = shopName.value,
            onValueChange = { shopName.value = it },
            label = { Text("Shop Name") }
        )
        OutlinedTextField(
            value = backendUrl.value,
            //prefix = { Text("https://") },
            onValueChange = { backendUrl.value = it },
            label = { Text("Backend URL") },
            leadingIcon = { Icon(Icons.Filled.Favorite, contentDescription = "DD")},

        )
        Button(onClick = {
            preferencesManager.saveData("shopName", shopName.value)
        }) {
            Text("Save")
        }
    }
}