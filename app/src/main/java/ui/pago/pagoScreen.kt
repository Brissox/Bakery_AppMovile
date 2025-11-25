package ui.pago

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagoScreen(
    pagoViewModel: PagoViewModel = viewModel(),
    onCompraExitosa: () -> Unit
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



    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {

        Text("Resumen de compra", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))


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
            // Corrección: Uso correcto de KeyboardOptions
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fechaEntrega,
            onValueChange = {},
            label = { Text("Fecha estimada de entrega") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(pagoViewModel.cartItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Corrección: 'producto' en singular
                        Text("Producto: ${item.productos.nombre}")
                        Text("Cantidad: ${item.cantidad}")
                        Text("Precio unitario: $${item.productos.precio}")
                        // Corrección: Cálculo directo con el precio (Long)
                        Text("Subtotal: $${item.productos.precio * item.cantidad}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
            "Total a pagar: $${pagoViewModel.total}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                pagoViewModel.realizarPago(
                    direccion = direccion,
                    recibe = recibe,
                    contacto = contacto
                )
                mostrarPopup = true   // ← Abre popup
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = direccion.isNotBlank() && recibe.isNotBlank() && contacto.isNotBlank()
        ) {
            Text("Pagar")
        }
        }
    if (mostrarPopup) {
        AlertDialog(
            onDismissRequest = { mostrarPopup = false },
            confirmButton = {
                TextButton(onClick = {
                    mostrarPopup = false
                    onCompraExitosa()   // ← Navegas o cierras pantalla
                }) {
                    Text("Aceptar")
                }
            },
            title = { Text("Pago realizado") },
            text = { Text("¡Tu pago ha sido procesado con éxito!") }
        )
    }
    }



@Preview(showBackground = true)
@Composable
private fun PagoScreenPreview() {
    PagoScreen(onCompraExitosa = {})
}