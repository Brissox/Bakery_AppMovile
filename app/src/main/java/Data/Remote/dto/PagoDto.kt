package Data.Remote.dto

import java.sql.Timestamp
import java.util.Date

data class PagoDto (

    val MONTO: Int,
    val FECHA_PAGO: Timestamp,
    val METODO_PAGO: String,
    val ESTADO_PAGO: String


)