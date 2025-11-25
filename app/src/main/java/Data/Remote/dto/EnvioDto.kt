package Data.Remote.dto

import java.sql.Timestamp

data class EnvioDto (

    val ID_PEDIDO: Int,
    val DIRECCION_ENVIO: String,
    val FECHA_ENVIO:     Timestamp,
    val DESCRIPCION:     String
)