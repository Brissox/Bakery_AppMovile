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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.prueba.ui.carrito.CartViewModel
import com.example.prueba.ui.principal.BottomItem
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel()
) {
    var direccion by remember { mutableStateOf("") }
    var recibe by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf("") }
    val fechaEntrega = remember {
        LocalDate.now().plusDays(7)
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    var mostrarPopup by remember { mutableStateOf(false) }

    val opcionesPago = listOf(
        "Tarjeta de Credito",
        "Tarjeta de Debito",
        "Tarjeta Pregago"
    )
    var expandedMetodoPago by remember { mutableStateOf(false) }


    var pagoRealizado by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val total = cartViewModel.cartItems.sumOf { it.productos.precio * it.cantidad }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text("Pago", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

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
                    label = { Text("Metodo de Pago") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMetodoPago)
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

            Text(
                "Total a pagar: $$total",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    pagoRealizado = true

                    cartViewModel.clearCart()

                    scope.launch {
                        snackbarHostState.showSnackbar("Pago realizado con éxito 🎉")
                    }

                    scope.launch {
                        kotlinx.coroutines.delay(1500)
                        navController.navigate(BottomItem.Home.route) {
                            popUpTo(ui.app.Route.Principal.path) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = direccion.isNotBlank() &&
                        recibe.isNotBlank() &&
                        contacto.isNotBlank()
            ) {
                Text("Pagar ahora")
            }

            if (pagoRealizado) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Procesando pago...",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
