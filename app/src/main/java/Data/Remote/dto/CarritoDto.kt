package Data.Remote.dto

import java.sql.Timestamp

data class CarritoDto (

    val FECHA_CREACION: Timestamp,
    val TOTAL: Int,
    val ESTADO: Char,

    val CANTIDAD: Int,
    val PRECIO_UNITARIO: Int,
    val SUBTOTAL: Int
)