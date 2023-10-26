package com.bnuttin.s700pos.pages

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.bnuttin.s700pos.api.QrCodeAnalyzer
import com.bnuttin.s700pos.models.SettingsViewModel
import org.json.JSONObject

@Composable
fun QRSCanner(settingsViewModel: SettingsViewModel, navController: NavHostController) {
    var context = LocalContext.current
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
                Manifest.permission.CAMERA
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
        launcher.launch(Manifest.permission.CAMERA)
    }



    Column(
        modifier = Modifier
            .padding(top = 70.dp, start = 10.dp, end = 10.dp)
    ) {
        Text(
            text = "QR Scanner",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )

        if (hasCamPermission){
            AndroidView(
                factory = { context ->
                    val previewView = PreviewView(context)
                    val preview = Preview.Builder().build()
                    val selector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                    val imageAnalysis = ImageAnalysis.Builder()
                        //.setTargetResolution(Size(previewView.width, previewView.height))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        QrCodeAnalyzer { result ->
                            code = result
                            val json = JSONObject(code)
                            settingsViewModel.updateSellerName(json.getString("seller"))
                            settingsViewModel.updateBackendUrl(json.getString("backend"))
                            navController.navigate("settings")
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
                },
                modifier = Modifier.padding(start = 0.dp, top = 45.dp, end = 0.dp, bottom = 8.dp)
            )
        }
        else{
            Text("No Camera permissions")
        }

    }
}