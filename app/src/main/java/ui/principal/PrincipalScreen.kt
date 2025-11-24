package com.example.prueba.ui.principal

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.prueba.ui.principal.components.UiProductosCard
import com.example.prueba.ui.profile.ProfileScreen
import com.example.prueba.ui.profile.ProfileViewModel
import com.example.prueba.repository.auth.FirebaseAuthDataSource
import com.example.prueba.data.media.MediaRepository
import com.example.prueba.ui.carrito.CarritoScreen
import com.example.prueba.ui.carrito.CartViewModel
import com.example.prueba.vmfactory.ProfileVMFactory
import ui.Fav.FavoritosViewModel
import ui.feriados.FeriadoViewModel

// --- Bottom items ---
sealed class BottomItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val badge: Int? = null
) {
    data object Home : BottomItem("home", "Inicio", Icons.Outlined.Home)
    data object Favs : BottomItem("favs", "Favoritos", Icons.Outlined.FavoriteBorder)
    data object Cart : BottomItem("cart", "Carrito", Icons.Outlined.ShoppingCart, badge = 3)
    data object Agenda : BottomItem("agenda", "Agenda", Icons.Outlined.PlayArrow)
    data object More : BottomItem("more", "Más", Icons.Outlined.Menu)

    data object Feriados : BottomItem("feriados", "Feriados", Icons.Outlined.Info)
}

private val bottomItems = listOf(
    BottomItem.Home, BottomItem.Favs, BottomItem.Cart, BottomItem.Agenda,  BottomItem.Feriados , BottomItem.More
)

@Composable
private fun BottomBar(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    onHomeTap: () -> Unit
) {
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route
    
    // Calculamos el badge total
    val totalItems = cartViewModel.cartItems.sumOf { it.cantidad }

    NavigationBar {
        bottomItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (item.route == BottomItem.Home.route) {
                        onHomeTap()
                        navController.navigate(BottomItem.Home.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = false }
                            launchSingleTop = true
                            restoreState = false
                        }
                    } else {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    if (item == BottomItem.Cart && totalItems > 0) {
                        BadgedBox(
                            badge = { Badge { Text("$totalItems") } }
                        ) {
                            Icon(item.icon, contentDescription = item.title)
                        }
                    } else {
                        Icon(item.icon, contentDescription = item.title)
                    }
                },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PrincipalScreen(
    onLogout: () -> Unit = {},
    onCheckout: () -> Unit = {},
    vm: PrincipalViewModel = viewModel()
) {
    val state by vm.ui.collectAsState()
    val categoriaSel by vm.categoriaSel.collectAsState()
    val productos by vm.productosFiltrados.collectAsState()
    val categorias by vm.categorias.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val tabsNav = rememberNavController()
    
    // ViewModels compartidos
    val cartViewModel: CartViewModel = viewModel()
    val favoritosViewModel: FavoritosViewModel = viewModel()

    LaunchedEffect(state.loggedOut) {
        if (state.loggedOut) onLogout()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.error) {
        state.error?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Principal") },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Outlined.MoreVert, contentDescription = "Menú")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Perfil") },
                            onClick = {
                                expanded = false
                                tabsNav.navigate("profile")
                            },
                            leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Configuración") },
                            onClick = { expanded = false },
                            leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                expanded = false
                                vm.logout()
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                navController = tabsNav,
                cartViewModel = cartViewModel, // Pasamos el VM real
                onHomeTap = { vm.refreshHome() }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { inner ->
        NavHost(
            navController = tabsNav,
            startDestination = BottomItem.Home.route,
            modifier = Modifier.padding(inner)
        ) {
            // HOME
            composable(route = BottomItem.Home.route) {
                LaunchedEffect(Unit) {
                    if (productos.isEmpty()) vm.cargarProductos()
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val saludo = "Hola ${state.nombre}"
                    Text(saludo, style = MaterialTheme.typography.headlineSmall)
                    Text("Bienvenido a tu pantalla principal.")

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(categorias.size) { idx ->
                            val cat = categorias[idx]
                            FilterChip(
                                selected = categoriaSel == cat,
                                onClick = { vm.setCategoria(cat) },
                                label = { Text(cat) }
                            )
                        }
                    }

                    if (state.loading) {
                        Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else if (state.error != null) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error: ${state.error}", color = Color.Red, textAlign = TextAlign.Center)
                        }
                    } else if (productos.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No se encontraron productos.")
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
                        ) {
                            items(productos, key = { it.id_producto }) { producto ->
                                // Reactivamos los parámetros aquí
                                UiProductosCard(
                                    producto = producto,
                                    cartViewModel = cartViewModel,
                                    esFavorito = favoritosViewModel.esFavorito(producto),
                                    onFavoritoClick = { favoritosViewModel.toggleFavorito(producto) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }

            // FAVORITOS (Ahora funcional)
            composable(BottomItem.Favs.route) {
                val favoritos = favoritosViewModel.favoritos
                if (favoritos.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No tienes productos favoritos aún.")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(favoritos, key = { it.id_producto }) { producto ->
                            UiProductosCard(
                                producto = producto,
                                cartViewModel = cartViewModel,
                                esFavorito = true,
                                onFavoritoClick = { favoritosViewModel.toggleFavorito(producto) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // CARRITO
            composable(BottomItem.Cart.route) {
                CarritoScreen(cartViewModel = cartViewModel,
                    onPagarClick = onCheckout )
            }
            
            // ... (Agenda y Más quedan igual)
            composable(BottomItem.Agenda.route) {
                val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                if (uid == null) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Debes iniciar sesión para ver tus recordatorios.")
                    }
                } else {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    val factory = remember(uid) { com.example.prueba.ui.vmfactory.RecordatorioVMFactory(context, uid) }
                    val rvm: com.example.prueba.ui.recordatorio.RecordatorioViewModel =
                        androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
                    com.example.prueba.ui.recordatorio.RecordatorioScreen(rvm)
                }
            }

            // FERIADOS
            composable(BottomItem.Feriados.route) {
                // ViewModel sin factory (tiene constructor por defecto usando el repo)
                val fvm: FeriadoViewModel = viewModel()
                ui.feriados.FeriadoScreen(viewModel = fvm)
            }

            composable(BottomItem.More.route) {
                Column(
                    Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    Text("Más opciones")
                    Button(onClick = { vm.logout() }) {
                        Icon(Icons.Outlined.Close, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (state.loading) "Cerrando..." else "Cerrar sesión")
                    }
                }
            }

            composable("profile") {
                val authDs = remember { Data.repository.FirebaseAuthDataSource() }
                val mediaRepo = remember { MediaRepository() }
                val factory = remember { ProfileVMFactory(authDs, mediaRepo) }
                val pvm: ProfileViewModel = viewModel(factory = factory)
                ProfileScreen(pvm)
            }
        }
    }
}