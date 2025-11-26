    package Data.Remote.dto


    data class PedidoDto(

        val idUsuario: Long,
        val fecha: String,
        val cantidad_productos: Int,
        val total: Int,
        val metodo_de_pago: String,
        val descuentos: Int?,
        val detalles: List<DetallePedidoDTO>
    )

    data class DetallePedidoDTO(
        val idProducto: Long,
        val cantidad: Int,
        val precioUnitario: Double,
        val subtotal: Double
    )