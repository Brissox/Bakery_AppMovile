package Data.Remote.dto




data class PedidoResp(
    val idUsuario: Long,
    val fecha: String,
    val cantidad_productos: Int,
    val total: Int,
    val metodo_de_pago: String,
    val descuentos: Int?,
    val detalles: List<DetallePedidot>
)

data class DetallePedidot(
    val idProducto: Long,
    val cantidad: Int,
    val precioUnitario: Int,
    val subtotal: Int
)