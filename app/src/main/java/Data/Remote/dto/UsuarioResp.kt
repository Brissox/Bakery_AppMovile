package Data.Remote.dto


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