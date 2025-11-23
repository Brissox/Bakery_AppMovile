package Data.Remote.dto


// Modelo intermedio para enviar datos entre la app Android y tu backend
data class UsuarioDto(
    val RUN: Int,
    val DV: String,
    val USUARIO: String,
    val CORREO: String,
    val CONTRASENA: String,
    val U_ID: String,
    val FECHA_NACIMIENTO: String,
    val Estado: String = "A",
    val ID_ROL: Int = 2

)

