@file:OptIn(ExperimentalMaterial3Api::class)

package com.bnuttin.s700pos.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bnuttin.s700pos.models.SettingsViewModel

//@OptIn(ExperimentalMaterial3Api::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(settingsViewModel: SettingsViewModel) {
    var shareMsg by remember { mutableStateOf("") }

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
        Divider()
        Text("From storage: " + settingsViewModel.getShareMsg())
        OutlinedTextField(
            value = shareMsg,
            onValueChange = { shareMsg = it },
            label = { Text("Share Msg") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                settingsViewModel.updateShareMsg(shareMsg)
            }
        ){
            Text("Update Share Msg")
        }
        Divider()
        Button(
            onClick = {
                settingsViewModel.getConnectionToken()
            },
            shape = RoundedCornerShape(size = 6.dp),
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
        ) {
            if (settingsViewModel.statusConnectionToken == "loading") {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            } else {
                Text("Get Connection Token")
            }
        }
        Text("Connection token: " + settingsViewModel.connectionToken.secret)
    }
}