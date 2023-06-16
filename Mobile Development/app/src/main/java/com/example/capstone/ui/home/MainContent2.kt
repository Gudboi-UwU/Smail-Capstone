package com.example.capstone.ui.home

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.capstone.R
import com.example.capstone.ui.camera.CameraCapture
import com.example.capstone.ui.detail.DetailImage
import com.example.capstone.ui.gallery.GallerySelect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainContent2(navController: NavController) {
    val EMPTY_IMAGE_URI = remember { mutableStateOf(Uri.parse("file://dev/null")) }
    val imageUri = remember { mutableStateOf(EMPTY_IMAGE_URI.value) }
    var showGallerySelect by remember { mutableStateOf(false) }

    if (imageUri.value != EMPTY_IMAGE_URI.value) {
        Box(modifier = Modifier.size(700.dp)) {
            ImageWithRemoveAndUploadButton(
                modifier = Modifier.fillMaxSize(),
                imageUri = imageUri.value,
                onRemoveImage = {
                    imageUri.value = EMPTY_IMAGE_URI.value
                },
                onUploadImage = {

                },
                navController = navController
            )
        }
    } else {
        if (showGallerySelect) {
            GallerySelect(
                modifier = Modifier.fillMaxSize(),
                onImageUri = { uri ->
                    showGallerySelect = false
                    imageUri.value = uri
                }
            )
        } else {
            Box(modifier = Modifier.size(700.dp)) {
                CameraCapture(
                    modifier = Modifier.size(900.dp),
                    onImageFile = { file ->
                        imageUri.value = file.toUri()
                    }
                )
                Button(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(4.dp),
                    onClick = {
                        showGallerySelect = true
                    }
                ) {
                    Text("Select from Gallery")
                }
            }
        }
    }
}

@Composable
private fun ImageWithRemoveAndUploadButton(
    modifier: Modifier = Modifier,
    imageUri: Uri,
    onRemoveImage: () -> Unit,
    onUploadImage: () -> Unit,
    navController: NavController
) {
    val painter = rememberImagePainter(imageUri)
    val currentDate = remember {
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        dateFormat.format(Date())
    }

    Column(modifier = modifier.padding(16.dp)) {
        Image(
            painter = painterResource(R.drawable.martbg), // Replace `R.drawable.martbg` with the actual resource ID of your background image
            contentDescription = "Smail Mart Background",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 16.dp)
        )

        Text(text = "ID: A157H", style = MaterialTheme.typography.h6)
        Text(text = "Date: $currentDate", style = MaterialTheme.typography.body1)

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Product: ", modifier = Modifier.weight(1f))
            Image(
                painter = painter,
                contentDescription = "Captured image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 8.dp, start = 8.dp)
                    .clip(shape = RoundedCornerShape(4.dp)),
                contentScale = ContentScale.FillBounds
            )
        }

        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Name: ", modifier = Modifier.weight(1f))
            Text(text = "Nabati Wafer 50g", style = MaterialTheme.typography.caption)
        }

        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Price: ", modifier = Modifier.weight(1f))
            Text(text = "2000", style = MaterialTheme.typography.caption)
        }

        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Quantity: ", modifier = Modifier.weight(1f))
            Text(text = "1", style = MaterialTheme.typography.caption)
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Total: ", modifier = Modifier.weight(1f), style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold))
            Text(text = "2000", style = MaterialTheme.typography.caption)
        }

        Button(
            onClick = { navController.navigate("detailImage") },
            modifier = Modifier
                .size(width = 200.dp, height = 50.dp)
                .background(androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor("#124BBA"))), // Warna latar belakang tombol menggunakan nilai heksadesimal
            colors = ButtonDefaults.buttonColors(backgroundColor = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor("#309464"))), // Warna teks tombol menggunakan nilai heksadesimal
        ) {
            Text(text = "Bayar")
        }
    }
}

@Composable
fun AppContent() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "mainContent2") {
        composable("mainContent2") { MainContent2(navController) }
        composable("detailImage") { DetailImage(navController) }
    }
}

