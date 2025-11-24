package Data.Remote.dto

import java.util.Date

data class PedidoResp (
    val FECHA: Date,
    val CANTIDAD_PRODUCTOS: Int,
    val TOTAL: Int,
    val METODO_DE_PAGO: String,
    val Descuentos: Int,
    val CANTIDAD: Int,
    val PRECIO_UNITARIO: Int,
    val SUBTOTAL: Int
)
