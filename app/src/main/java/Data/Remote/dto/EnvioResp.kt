package Data.Remote.dto

import java.sql.Timestamp
import java.util.Date

data class EnvioResp (
    val DIRECCION_ENVIO: String,
    val FECHA_ENVIO: Timestamp,
    val FECHA_ENTREGA: Timestamp,
    )