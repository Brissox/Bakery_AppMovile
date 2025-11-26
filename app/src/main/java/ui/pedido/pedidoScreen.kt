package ui.pedido

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Datos de prueba
data class PedidoDetalle(
    val idProducto: Long,
    val cantidad: Int,
    val precioUnitario: Double
) {
    val subtotal: Double get() = cantidad * precioUnitario
}

data class Pedido(
    val fechaCreacion: LocalDateTime?,
    val cantidadProductos: Int,
    val descuentos: Double?,
    val metodoPago: String,
    val total: Double?,
    val items: List<PedidoDetalle>
)

@Composable
fun PedidoScreen() {
    // Lista de ejemplo
    val pedidos = listOf(
        Pedido(
            fechaCreacion = LocalDateTime.now(),
            cantidadProductos = 3,
            descuentos = 10.0,
            metodoPago = "Tarjeta de Crédito",
            total = 90.0,
            items = listOf(
                PedidoDetalle(idProducto = 1, cantidad = 1, precioUnitario = 30.0),
                PedidoDetalle(idProducto = 2, cantidad = 2, precioUnitario = 35.0)
            )
        ),
        Pedido(
            fechaCreacion = LocalDateTime.now().minusDays(1),
            cantidadProductos = 2,
            descuentos = 0.0,
            metodoPago = "Tarjeta Débito",
            total = 50.0,
            items = listOf(
                PedidoDetalle(idProducto = 3, cantidad = 2, precioUnitario = 25.0)
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Mis pedidos",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(pedidos) { pedido ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        val fechaFormateada = pedido.fechaCreacion?.format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                        ) ?: "Sin fecha"

                        Text("Fecha: $fechaFormateada")
                        Text("Cantidad de productos: ${pedido.cantidadProductos}")
                        Text("Descuentos: $${pedido.descuentos ?: 0.0}")
                        Text("Método de pago: ${pedido.metodoPago}")
                        Text(
                            "Total final: $${pedido.total ?: 0.0}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "Detalles del pedido:",
                            style = MaterialTheme.typography.titleSmall
                        )

                        pedido.items.forEach { detalle ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text("Producto ID: ${detalle.idProducto}")
                                Text("Cantidad: ${detalle.cantidad}")
                                Text("Precio unitario: $${detalle.precioUnitario}")
                                Text("Subtotal: $${detalle.subtotal}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PedidoScreenPreview() {
    PedidoScreen()
}
