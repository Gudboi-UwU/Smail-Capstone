package com.example.capstone

import CameraActivity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.capstone.ui.camera.CameraCapture
import com.example.capstone.ui.detail.DetailImage
import com.example.capstone.ui.gallery.GallerySelect
import com.example.capstone.ui.home.Dashboard
import com.example.capstone.ui.home.MainContent2


import com.example.capstone.ui.login.LoginScreen
import com.example.capstone.ui.register.RegisterScreen
import com.example.capstone.ui.theme.CapstoneTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import java.util.concurrent.ExecutorService


class MainActivity : ComponentActivity() {

    private var shouldShowCamera: MutableState<Boolean> = mutableStateOf(false)
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraHandler: CameraActivity
    private lateinit var photoUri: Uri
    private var shouldShowPhoto: MutableState<Boolean> = mutableStateOf(false)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.i("kilo", "Permission granted")
            shouldShowCamera.value = true
        } else {
            Log.i("kilo", "Permission denied")
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CapstoneTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
////
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController) }
                        composable("dashboard") { Dashboard(navController) }
                        composable("mainContent2") { MainContent2(navController) }
                        composable("detailImage") { DetailImage(navController) }
                        composable("register") { RegisterScreen(navController) }
//                    MainContent(Modifier.fillMaxSize())

                    }
                }
            }
        }
    }
}

//    @ExperimentalCoilApi
//    @ExperimentalCoroutinesApi
//    @ExperimentalPermissionsApi
//    @Composable
//    fun MainContent(modifier: Modifier = Modifier) {
//        var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }
//        if (imageUri != EMPTY_IMAGE_URI) {
//            Box(modifier = modifier) {
//                Image(
//                    modifier = Modifier.fillMaxSize(),
//                    painter = rememberImagePainter(imageUri),
//                    contentDescription = "Captured image"
//                )
//                Button(
//                    modifier = Modifier.align(Alignment.BottomCenter),
//                    onClick = {
//                        imageUri = EMPTY_IMAGE_URI
//                    }
//                ) {
//                    Text("Remove image")
//                }
//            }
//        } else {
//            var showGallerySelect by remember { mutableStateOf(false) }
//            if (showGallerySelect) {
//                GallerySelect(
//                    modifier = modifier,
//                    onImageUri = { uri ->
//                        showGallerySelect = false
//                        imageUri = uri
//                    }
//                )
//            } else {
//                Box(modifier = modifier) {
//                    CameraCapture(
//                        modifier = modifier,
//                        onImageFile = { file ->
//                            imageUri = file.toUri()
//                        }
//                    )
//                    Button(
//                        modifier = Modifier
//                            .align(Alignment.TopCenter)
//                            .padding(4.dp),
//                        onClick = {
//                            showGallerySelect = true
//                        }
//                    ) {
//                        Text("Select from Gallery")
//                    }
//                }
//            }
//        }
//    }
//
//    val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")




//                    Register()

//                    DashboardScreen()
//                    CameraActivity().requestCameraPermission()
//                    if (shouldShowCamera.value) {
//                        CameraView(
//                            outputDirectory = outputDirectory,
//                            executor = cameraExecutor,
//                            onImageCaptured = ::handleImageCapture,
//                            onError = { Log.e("kilo", "View error:", it) }
//                        )
//                    }
//
//                    if (shouldShowPhoto.value) {
//                        Image(
//                            painter = rememberImagePainter(photoUri),
//                            contentDescription = null,
//                            modifier = Modifier.fillMaxSize()
//                        )
//                    }
//                }
//
//                requestCameraPermission()
//
//                outputDirectory = getOutputDirectory()
//                cameraExecutor = Executors.newSingleThreadExecutor()
//            }


//    private fun requestCameraPermission() {
//        when {
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED -> {
//                Log.i("kilo", "Permission previously granted")
//                shouldShowCamera.value = true
//            }
//
//            ActivityCompat.shouldShowRequestPermissionRationale(
//                this,
//                Manifest.permission.CAMERA
//            ) -> Log.i("kilo", "Show camera permissions dialog")
//
//            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
//        }
//    }
//
//    private fun handleImageCapture(uri: Uri) {
//        Log.i("kilo", "Image captured: $uri")
//        shouldShowCamera.value = false
//
//        photoUri = uri
//        shouldShowPhoto.value = true
//    }
//
//    private fun getOutputDirectory(): File {
//        val mediaDir = externalMediaDirs.firstOrNull()?.let {
//            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
//        }
//
//        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        cameraExecutor.shutdown()
//    }
