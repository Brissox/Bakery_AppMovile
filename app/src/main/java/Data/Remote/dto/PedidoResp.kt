package Data.Remote.dto



import java.time.LocalDateTime

data class PedidoResp(
    val id: Long,
    val idUsuario: Long,
    val fechaCreacion: LocalDateTime,
    val cantidadProductos: Int,
    val metodoPago: String,
    val descuentos: Int = 0,
    val total: Int,
    val estado: String,
    val items: List<ItemPedidoResp>
)

data class ItemPedidoResp(
    val id: Long,
    val idProducto: Long,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Int,
    val subtotal: Int
)