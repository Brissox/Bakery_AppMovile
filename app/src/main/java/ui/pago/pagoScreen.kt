package ui.pago

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.prueba.ui.carrito.CartViewModel
import com.example.prueba.ui.principal.BottomItem
import kotlinx.coroutines.launch
import ui.app.AppViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel(),
    appViewModel: AppViewModel = viewModel()
) {
    val pagoViewModel: PagoViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PagoViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return PagoViewModel(appViewModel, cartViewModel.cartItems.toList()) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )

    LaunchedEffect(cartViewModel.cartItems) {
        pagoViewModel.actualizarCarrito(cartViewModel.cartItems)
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var direccion by remember { mutableStateOf("") }
    var recibe by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf("") }
    var procesandoPago by remember { mutableStateOf(false) }

    val total = pagoViewModel.cartItems.sumOf { it.productos.precio * it.cantidad }
    val opcionesPago = listOf("Tarjeta de Credito", "Tarjeta de Debito", "Tarjeta Prepago")
    var expandedMetodoPago by remember { mutableStateOf(false) }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Datos de despacho", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección de despacho") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = recibe,
                onValueChange = { recibe = it },
                label = { Text("Nombre de quien recibe") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = contacto,
                onValueChange = { contacto = it },
                label = { Text("Número de contacto") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expandedMetodoPago,
                onExpandedChange = { expandedMetodoPago = !expandedMetodoPago }
            ) {
                OutlinedTextField(
                    value = metodoPago,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Método de Pago") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMetodoPago) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedMetodoPago,
                    onDismissRequest = { expandedMetodoPago = false }
                ) {
                    opcionesPago.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                metodoPago = opcion
                                expandedMetodoPago = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Productos en carrito:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            pagoViewModel.cartItems.forEach { item ->
                Text("${item.productos.nombre} x${item.cantidad} - \$${item.productos.precio * item.cantidad}")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Total a pagar: \$${total}", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    scope.launch {
                        procesandoPago = true
                        try {
                            val idUsuario = appViewModel.idUsuario.value
                            if (idUsuario != null) {
                                pagoViewModel.realizarPagoConIdUsuario(
                                    metodoDePago = metodoPago,
                                    descuentos = 0
                                )

                                cartViewModel.clearCart()
                                snackbarHostState.showSnackbar("Pago realizado con éxito 🎉")

                                kotlinx.coroutines.delay(500)
                                navController.navigate("home") {
                                    popUpTo("principal") { inclusive = true }
                                    launchSingleTop = true
                                }
                            } else {
                                snackbarHostState.showSnackbar("No se encontró usuario activo")
                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Error al procesar el pago: ${e.message}")
                        } finally {
                            procesandoPago = false
                        }
                    }
                },
                enabled = direccion.isNotBlank() && recibe.isNotBlank() &&
                        contacto.isNotBlank() && metodoPago.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pagar ahora")
            }

            if (procesandoPago) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("Procesando pago...", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}