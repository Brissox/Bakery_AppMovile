package Data.Remote.dto


// Modelo de respuesta que representa lo que el backend devuelve al cliente
data class UsuarioResp(


    val run: String,
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
    val imagen: String?,
    /*
        val U_ID: String,
    val NOMBRE: String = "",
    val APELLIDO_PATERNO: String? = null,
    val APELLIDO_MATERNO: String? = null,
    val USUARIO: String = "",
    val CORREO: String  = "",
    val CONTRASENA: String  = "",
    val TELEFONO: Int = 0,
    val FECHA_NACIMIENTO: String ="",
    val PAIS: String? = null,
    val CIUDAD: String? = null,
    val ID_ROL: Int = 2,
    val DIRECCION: String? = null,
    val CODIGO_DESC: String? = null,
    val RUN: String,
    val DV: String = "",
    val ESTADO: String = "A",
    val IMAGEN: String?
    */

)

