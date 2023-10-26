@file:OptIn(ExperimentalMaterial3Api::class)

package com.bnuttin.s700pos.pages

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.bnuttin.s700pos.api.QrCodeAnalyzer
import com.bnuttin.s700pos.datastore.PrefRepository
import com.bnuttin.s700pos.models.SettingsViewModel

//@OptIn(ExperimentalMaterial3Api::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(settingsViewModel: SettingsViewModel) {
    var context = LocalContext.current
    val prefRepository = PrefRepository(context)
    var sellerName by remember { mutableStateOf(prefRepository.getSellerName()) }
    var currency by remember { mutableStateOf(prefRepository.getCurrency()) }
    var backendUrl by remember { mutableStateOf(prefRepository.getBackendUrl()) }

    var code by remember {
        mutableStateOf("")
    }
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )
    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = true) {
        launcher.launch(android.Manifest.permission.CAMERA)
    }



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
            value = sellerName,
            onValueChange = { sellerName = it },
            label = { Text("Seller Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = currency,
            onValueChange = { currency = it },
            label = { Text("Currency") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = backendUrl,
            onValueChange = { backendUrl = it },
            label = { Text("Backend URL") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                prefRepository.setSellerName(sellerName)
                settingsViewModel.updateCurrency(currency)
                settingsViewModel.updateBackendUrl(backendUrl)
            }
        ){
            Text("Save")
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
        Text("Code is: " + code)
        Button(
            onClick = {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse("stripe://settings/"))
                )
            },
            shape = RoundedCornerShape(size = 6.dp),
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 0.dp, bottom = 8.dp)
        ) {
            Text("Device Settings")
        }

        if (hasCamPermission){

            AndroidView(
                factory = {context ->
                val previewView = PreviewView(context)
                val preview = Preview.Builder().build()
                val selector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                preview.setSurfaceProvider(previewView.surfaceProvider)
                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(previewView.width, previewView.height))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    QrCodeAnalyzer { result ->
                        code = result
                        settingsViewModel.updateSellerName(result)
                    }
                )
                try {
                    cameraProviderFuture.get().bindToLifecycle(
                        lifeCycleOwner,
                        selector,
                        preview,
                        imageAnalysis
                    )
                } catch(e: Exception) {
                    e.printStackTrace()
                }
                previewView
            })

        }



    }
}