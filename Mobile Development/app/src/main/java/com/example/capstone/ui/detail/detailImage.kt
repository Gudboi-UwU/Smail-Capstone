package com.example.capstone.ui.detail
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.capstone.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DetailImage(navController: NavController) {
    var total by remember { mutableStateOf("") }
    var paid by remember { mutableStateOf("") }
    var change by remember { mutableStateOf("") }
    val nameInput = remember { mutableStateOf("") }

    val currentDate = remember {
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        dateFormat.format(Date())
    }

    Column(modifier = Modifier.padding(16.dp)) {
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
            Text("Total: ", modifier = Modifier.weight(1f))
            TextField(
                value = total,
                onValueChange = { total = it },
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Paid:", modifier = Modifier.weight(1f))
            TextField(
                value = paid,
                onValueChange = { paid = it },
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Change:", modifier = Modifier.weight(1f))
            TextField(
                value = change,
                onValueChange = { change = it },
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .size(width = 200.dp, height = 50.dp)
                .background(androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor("#124BBA"))), // Warna latar belakang tombol menggunakan nilai heksadesimal
            colors = ButtonDefaults.buttonColors(backgroundColor = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor("#309464"))), // Warna teks tombol menggunakan nilai heksadesimal
        ) {
            Text(text = "Finish")
        }
    }
}




