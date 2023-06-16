package  com.example.capstone.ui.home


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.capstone.R
import com.example.capstone.ui.detail.DetailImage
import com.example.capstone.ui.login.LoginScreen
import com.example.capstone.ui.register.RegisterScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

data class Product(val name: String, val quantity: String, val price: Int, val date: String)

@Composable
fun ProductList(products: List<Product>) {
    LazyColumn {
        items(products) { product ->
            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.menu_3),
                        contentDescription = "Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color(0xFF309464)
                    )
                    Text(
                        text = product.date,
                        style = TextStyle(fontSize = 16.sp, color = Color(0xFF309464)),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Text(text = " ${product.name}", fontSize = 14.sp)
                Text(text = " ${product.quantity}", fontSize = 14.sp)
                Text(text = "${product.price}", fontSize = 14.sp)

                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalCoilApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Dashboard(navController: NavController) {
    val navController = rememberNavController()
    val productList = remember {
        listOf(
            Product("Nabati Wafer 50g (1)", "", 2000, "June 16, 2023"),
            Product("Cleo (1)", "", 3000, "June 14, 2023"),
            Product("Mie Instan (5)", "", 4000, "June 13, 2023"),
            Product("Gula (1kg)", "", 8000, "June 13, 2023"),
            Product("Kopi Sachet (10)", "", 15000, "June 13, 2023"),
            Product("Cleo (1)", "", 3000, "June 13, 2023"),
            Product("Sampo (Sachet)", "", 3000, "June 12, 2023"),
            Product("Teh Botol (1)", "", 5000, "June 12, 2023"),
            Product("Snack (Variasi)", "", 8000, "June 12, 2023"),
            Product("Permen (3)", "", 2000, "June 11, 2023"),
            Product("Kacang (250g)", "", 6000, "June 11, 2023"),
            Product("Telur (6)", "", 10000, "June 11, 2023"),
            Product("Beng-beng (2)", "Chocolatos (1)", 5000, "June 9, 2023"),
            Product("Aqua (3)", "Nabati (1)", 15000, "June 9, 2023"),
            Product("Pensil (5)", "", 12000, "June 9, 2023"),
            Product("Susu (1)", "Milo (2)", 10000, "June 9, 2023"),
            Product("Sabun Mandi", "", 8000, "June 9, 2023"),
            Product("Shampoo", "", 12000, "June 9, 2023"),
            Product("Tissue (3)", "", 5000, "June 9, 2023"),
            Product("Minyak Goreng (2L)", "", 25000, "June 9, 2023"),
            Product("Sikat Gigi", "", 6000, "June 9, 2023")
        )
    }

    var selectedTab by remember { mutableStateOf(0) }
    var isFabClicked by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.fillMaxWidth()
            ) {
                BottomNavigationItem(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        navController.navigate("home")
                    },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )

                BottomNavigationItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController.navigate("profile")
                    },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        },
        floatingActionButton = {
            if (!isFabClicked) {
                FloatingActionButton(
                    onClick = {
                        isFabClicked = true
                        navController.navigate("mainContent2")
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.scan_qrcode),
                        contentDescription = "Camera"
                    )
                }
            }
        },
        isFloatingActionButtonDocked = true,
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.mart),
                                    contentDescription = "Mart Logo",
                                    modifier = Modifier.size(128.dp)
                                )

                                Divider(modifier = Modifier.padding(vertical = 16.dp))
                                Box(
                                    modifier = Modifier.align(Alignment.Start) // Align the product list to the start (left)
                                ) {
                                    Text(
                                        text = "Product Sales",
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier.align(Alignment.Start) // Align the product list to the start (left)
                                ) {
                                    ProductList(products = productList)
                                }
                            }
                        }
                    }

                    composable("profile") {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(R.drawable.martbg),
                                contentDescription = "Smail Mart Logo",
                                modifier = Modifier
                                    .size(128.dp)
                                    .padding(bottom = 16.dp)
                            )

                            Text(
                                text = "Smail Mart",
                                style = MaterialTheme.typography.h4,
                                modifier = Modifier.padding(bottom = 8.dp),
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Jalan Cendekia 18 Surabaya",
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                text = "Phone: 08199086567",
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                text = "Email: smailmart@gmail.com",
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }



                    composable("mainContent2") {
                        MainContent2(navController)
                        isFabClicked =
                            false // Reset the FAB clicked state when MainContent2 composable is shown
                    }

                    composable("detailImage") {
                        DetailImage(navController)
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalCoilApi::class)
@Preview
@Composable
fun DashboardPreview() {
    val navController = rememberNavController()
    Dashboard(navController)
}
