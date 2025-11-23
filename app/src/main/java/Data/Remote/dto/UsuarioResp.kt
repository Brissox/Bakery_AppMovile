package Data.Remote.dto


// Modelo de respuesta que representa lo que el backend devuelve al cliente
data class UsuarioResp(
    val run: Int,
    val dv: String,
    val usuario: String,
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val mail: String,
    val fechaNacimiento: String,
    val pais: String,
    val ciudad: String,
    val direccion: String,
    val telefono: Int,
    val uidFb: String?,
    val imagen: String?
)
