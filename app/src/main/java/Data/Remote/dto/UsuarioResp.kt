package Data.Remote.dto


// Modelo de respuesta que representa lo que el backend devuelve al cliente
data class UsuarioResp(

    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val usuario: String,
    val correo: String,
    val contrasena: String,
    val telefono: Int ,
    val fechaNacimiento: String,
    val pais: String ,
    val ciudad: String,
    val direccion: String ,
    val codigoDesc: String,
    val run: String,
    val dv: String,
    val imagen: String
)




