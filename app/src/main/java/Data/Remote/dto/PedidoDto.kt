    package Data.Remote.dto


    data class PedidoDto(
        val idUsuario: Long,
        val cantidad_productos: Int,
        val metodo_de_pago: String,
        val descuentos: Int = 0,
        val detalles: List<DetallePedidoDTO>
    )

    data class DetallePedidoDTO(
        val idProducto: Long,
        val cantidad: Int
    )
