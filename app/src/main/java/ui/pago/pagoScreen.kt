package ui.pago

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.prueba.repository.auth.FirebaseAuthDataSource
import com.example.prueba.ui.carrito.CartItem
import com.example.prueba.ui.carrito.CartViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.app.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoScreen(
    appViewModel: AppViewModel,
    navController: NavController,
    cartViewModel: CartViewModel,
    cartItems: List<CartItem>,
    authRepo: FirebaseAuthDataSource = FirebaseAuthDataSource()
) {
    // Factory solo se crea una vez
    val factory = remember(cartItems) {
        PagoVMFactory(
            authRepo = authRepo,
            cartItems = cartItems.toList()
        )
    }

    val pagoViewModel: PagoViewModel = viewModel(factory = factory)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var direccion by remember { mutableStateOf("") }
    var recibe by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf("") }
    var procesandoPago by remember { mutableStateOf(false) }

    val opcionesPago = listOf(
        "Tarjeta de Crédito",
        "Tarjeta de Débito",
        "Tarjeta Prepago"
    )

    var expandedMetodoPago by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

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
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expandedMetodoPago)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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

            Text("Productos en carrito", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar items del carrito
            pagoViewModel.cartItems.forEach { item ->
                Text(
                    "${item.productos.nombre} x${item.cantidad} - $${item.productos.precio * item.cantidad}"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Total a pagar: $${pagoViewModel.total.toInt()}",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = direccion.isNotBlank() &&
                        recibe.isNotBlank() &&
                        contacto.isNotBlank() &&
                        metodoPago.isNotBlank() &&
                        !procesandoPago,
                onClick = {
                    scope.launch {
                        procesandoPago = true
                        try {
                            pagoViewModel.realizarPagoConIdUsuario(
                                metodoDePago = metodoPago,
                                descuentos = 0
                            )
                            snackbarHostState.showSnackbar("✅ Pago realizado con éxito")
                            delay(800)
                            cartViewModel.clearCart()
                            navController.navigate("home") {
                                popUpTo("principal") { inclusive = true }
                                launchSingleTop = true
                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("❌ Error al pagar: ${e.message}")
                        } finally {
                            procesandoPago = false
                        }
                    }
                }
            ) {
                Text("Pagar ahora")
            }

            if (procesandoPago) {
                Spacer(modifier = Modifier.height(12.dp))
                CircularProgressIndicator()
            }
        }
    }
}
