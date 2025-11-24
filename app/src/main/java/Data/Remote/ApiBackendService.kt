package Data.Remote

import Data.Remote.dto.UsuarioDto
import Data.Remote.dto.UsuarioResp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiBackendService {

    @POST("Usuarios/Reg")
    suspend fun crearUsuario(@Body body: UsuarioDto): Response<ResponseBody>

    @GET("Usuarios/uid/{uidFb}")
    suspend fun getByFirebase(@Path("uidFb") uidFb: String): Response<UsuarioResp>

    @Multipart
    @PUT("Usuarios/{run}")
    suspend fun actualizarUsuarioConImagen(
        @Path("run") run: String,
        @Part("idFirebase") idFirebase: RequestBody,
        @Part imagen: MultipartBody.Part
    ): Response<ResponseBody>

    // --- Corrección aquí ---

    @GET("Usuarios/{rut}/imagen")
    suspend fun getImageByRut(@Path("rut") rut: String): Response<ResponseBody>

    @PUT("Usuarios/{rut}/nombre")
    suspend fun updateNombre(
        @Path("rut") rut: String,
        @Body body: Map<String, String>
    ): Response<UsuarioResp>
}