package Data.Remote.dto

data class UsuarioDto(
    val uidFb: String,
    val nombre: String,
    val apellidoPaterno: String = "",
    val apellidoMaterno: String = "",
    val usuario: String,
    val correo: String,
    val contrasena: String,
    val telefono: Int = 0,
    val fechaNacimiento: String,
    val pais: String = "",
    val ciudad: String = "",
    val direccion: String = "",
    val codigoDesc: String? = "",
    val run: String,
    val dv: String,
    val estado: String = "A",
    val imagen: String? = null,
    val rol: RolDto = RolDto(2)
)

data class RolDto(
    val idRol: Int = 2
)